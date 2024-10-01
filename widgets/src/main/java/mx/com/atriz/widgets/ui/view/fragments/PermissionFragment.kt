package mx.com.atriz.widgets.ui.view.fragments

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import mx.com.atriz.core.entities.Permission
import mx.com.atriz.core.entities.Permission.Type.BACKGROUND_LOCATION
import mx.com.atriz.core.entities.Permission.Type.LOCATION
import mx.com.atriz.core.ext.openBrowser
import mx.com.atriz.core.interfaces.AppInterface
import mx.com.atriz.widgets.R
import mx.com.atriz.widgets.databinding.DialogPermissionBinding

class PermissionFragment(
    private val permission: Permission
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogPermissionBinding
    private var listener: AppInterface? = null

    init {
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPermissionBinding.bind(
            inflater.inflate(R.layout.dialog_permission, container)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.text = getText(permission.title)
        binding.description.text = getText(permission.explanation)
        binding.background.setImageResource(permission.background ?: 0)
        binding.askPermission.setOnClickListener {
            if (permission.getPermissionList().size > 1) {
                multiplePermissionLauncher.launch(permission.getPermissionList())
            } else {
                singlePermissionLauncher.launch(permission.getPermissionList()[0])
            }
        }
        binding.privacy.setOnClickListener {
            openBrowser(permission.policyLink ?: "")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog
                .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { layout ->
                val behaviour = BottomSheetBehavior.from(layout)
                setupFullHeight(layout)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppInterface) {
            listener = context
        }
    }

    private val singlePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { singlePermission ->
        if (singlePermission) {
            listener?.onPermissionGranted(permission.type)
        } else {
            listener?.onPermissionDenied(permission.type)
        }
        dismiss()
    }

    private val multiplePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when (permission.type) {
            LOCATION -> {
                val fine = permissions
                    .getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
                val coarse = permissions
                    .getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
                if (fine || coarse) {
                    listener?.onPermissionGranted(LOCATION)
                } else {
                    listener?.onPermissionDenied(LOCATION)
                }
            }

            BACKGROUND_LOCATION -> {
                val fine = permissions
                    .getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)

                val coarse = permissions
                    .getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)

                val background = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    permissions
                        .getOrDefault(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false)
                } else {
                    true
                }

                if ((fine || coarse) && background) {
                    listener?.onPermissionGranted(BACKGROUND_LOCATION)
                } else {
                    listener?.onPermissionDenied(BACKGROUND_LOCATION)
                }
            }

            else -> {}
        }
        dismiss()
    }
}
