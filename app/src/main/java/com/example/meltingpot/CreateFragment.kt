package com.example.meltingpot

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.meltingpot.databinding.FragmentCreateBinding
import com.example.meltingpot.model.StandardPost
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import java.time.LocalDateTime
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [CreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateFragment : Fragment() {
    private lateinit var db: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private var lang_setting: String = "Spanish"
    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!
    var latlong: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            it.getString("LANG")?.let{ value -> lang_setting = value }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        db = Firebase.database
        binding.langPrompt.text = "Language: ${lang_setting}"

        //Create a location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        //get the last location identified. Also, set a listener that updates the
        //R.id.coordinates text when the location is found (on Success)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got the last known location. In some rare situations this can be null.
//                coordinates.text="Lat:${location?.latitude} Long:${location?.longitude}"
                Log.d("MainActivity", "Lat:${location?.latitude} Long:${location?.longitude}")

                // Step 8
                //we are using the let scope to avoid writing the if statements for this type of assignment
                location?.let{latlong=it}
            }

        // Disable the send button when there's no text in the input field
        // See MyButtonObserver for details
        binding.frTextIn.doOnTextChanged { textval: CharSequence?, /*start*/ _: Int, /*before*/ _: Int, /*count*/ _: Int ->
            binding.postFab.isEnabled = textval.toString().trim().isNotEmpty()
        }

        // When the send button is clicked, send a text message
        binding.postFab.setOnClickListener {
            var username: String = "anonymous"
            val user = auth.currentUser
            if (user != null) {
                user.displayName?.let{ username = it }
            }
            val timedateval: String = LocalDateTime.now().toString()
            var lati = 0.0
            var longi = 0.0
            latlong?.let{ lati = it.latitude
                longi = it.longitude
            }

            //translation
            val options: TranslatorOptions
            if(lang_setting.equals("French", true)){
                options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.FRENCH)
                    .setTargetLanguage(TranslateLanguage.ENGLISH)
                    .build()
            }
            else if(lang_setting.equals("Spanish", true)){
                options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.SPANISH)
                    .setTargetLanguage(TranslateLanguage.ENGLISH)
                    .build()
            }
            else if(lang_setting.equals("Japanese", true)){
                options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.JAPANESE)
                    .setTargetLanguage(TranslateLanguage.ENGLISH)
                    .build()
            }
            else{
                options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.ENGLISH)
                    .build()
            }
            val translator = Translation.getClient(options)
            lifecycle.addObserver(translator)

            var translated = ""
            var conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()
            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    // Model downloaded successfully. Okay to start translating.
                    // (Set a flag, unhide the translation UI, etc.)
                }
                .addOnFailureListener { exception ->
                    // Model couldn’t be downloaded or other internal error.
                    // ...
                }
            var translateString = binding.frTextIn.text.toString()
            translator.translate(translateString.toString())
                .addOnSuccessListener { translatedText ->
                    // Translation successful.
                    translated = translatedText
                }
                .addOnFailureListener { exception ->
                    // Error.
                    // ...
                    translated = "error when translating :("
                }

            val newPost: StandardPost = StandardPost(timedateval, "${lati},${longi}", lang_setting, binding.frTextIn.text.toString(), username, 0, translated)
            Log.d(TAG, "Adding post")
            db.reference.child("gen_posts").child(lang_setting).push().setValue(newPost)
            findNavController().navigate(R.id.action_createFragment_to_primaryFeed)
        }
        return binding.root
    }

    companion object {
        val TAG: String = "CreateFragment"
    }
}