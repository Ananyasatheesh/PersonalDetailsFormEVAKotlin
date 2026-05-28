package com.example.personaldetailsform_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment( private val userName: String, private val userEmail: String) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_bottom_sheet,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val btnEdit = view.findViewById<Button>(R.id.btnEdit)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)
        val btnView = view.findViewById<Button>(R.id.btnView)
        val btnShare = view.findViewById<Button>(R.id.btnShare)

        btnEdit.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Edit Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnDelete.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Delete Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnView.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "View Profile Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnShare.setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND)

            intent.type = "text/plain"

            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Name: $userName\nEmail: $userEmail"
            )

            startActivity(
                Intent.createChooser(intent, "Share User")
            )
        }
    }
}



// Fragment - Small portion in a activity (child of activity)
// Used for reusable code, code separation
// LIFECYCLE - 2 parts of fragment lifecycle
// * Fragment lifecycle - onAttach(), onCreate(), onDestroy(), onDetach()
// * Fragment View Lifecycle - onCreateView(), onViewCreated(), onDestroyView()
// onAttach() -> Attaches fragment to that respective Activity
// onCreate() -> Create the Fragment object
// onDestroy() -> Destroys the fragment object
// onDetach() -> Removes the attach to the activity
// onCreateView() -> Attach the respective view of fragment to that fragment object
// onViewCreated() -> After view is created, used to set listeners etc.,
// onDestroyView() -> Removes view from stack, but fragment object exists

// Fragment lifecylce is maintained separately for view and its obj because when rotation etc are done, only UI is destroyed and recreated again.

//ORDER OF FRAGMENT LIFECYCLE
//onAttach() -> onCreate() -> onCreateView() -> onViewCreated() -> onStart() -> onResume()
//
//USER USING SCREEN
//
//onPause() -> onStop() -> onDestroyView() -> onDestroy() -> onDetach()


// FIRST ACTIVITY LIFECYCLE EXECUTES, ONLY THEN FRAGMENT LIFECYCLE EXECUTES
// Activity onCreate() -> Fragment onAttach() -> Fragment onCreate() -> Fragment onCreateView() -> Fragment onViewCreated() -> Activity onStart() -> Fragment onStart() -> Activity onResume() -> Fragment onResume()

//FRAGMENTS ARE MANAGED BY FRAGMENT MANAGERS (rotation cycles, creation of lifecycles, deletion everything is managed)
// supportFragmentManager -> to access manager of Fragment Activity
// childFragmentManager -> to access manager of child fragment
// parentFragmentManager -> to access manager of parent fragment

// FRAGMENT TRANSACTION -> Tells what to do for a fragment manager