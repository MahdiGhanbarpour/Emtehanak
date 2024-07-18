package ir.mahdighanbarpour.khwarazmiapp.features.addExamQuestionScreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.mahdighanbarpour.khwarazmiapp.databinding.BottomSheetAddExamQuestionBinding
import ir.mahdighanbarpour.khwarazmiapp.features.addExamQuestionScreen.adapters.AddExamQuestionAttachmentAdapter
import ir.mahdighanbarpour.khwarazmiapp.features.addExamQuestionScreen.adapters.AddExamQuestionOptionAdapter
import ir.mahdighanbarpour.khwarazmiapp.model.data.AddQuestion
import ir.mahdighanbarpour.khwarazmiapp.model.data.AddQuestionAttachment
import ir.mahdighanbarpour.khwarazmiapp.model.data.Option
import ir.mahdighanbarpour.khwarazmiapp.utils.getFileFromUri
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast

class AddExamQuestionBottomSheet : BottomSheetDialogFragment(),
    AddExamQuestionAttachmentAdapter.AttachmentEvents,
    AddExamQuestionOptionAdapter.AddExamOptionEvents {

    private lateinit var binding: BottomSheetAddExamQuestionBinding
    private lateinit var addAttachmentAdapter: AddExamQuestionAttachmentAdapter
    private lateinit var addOptionAdapter: AddExamQuestionOptionAdapter
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private var pickedAttachmentImage: Uri? = null
    private var attachments = mutableListOf<AddQuestionAttachment>()
    private var options = mutableListOf<Option>()
    private var isSendingData = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAddExamQuestionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initAddAttachmentRecycler(attachments)
        initAddOptionRecycler(options)
        pickImageSetting()
        resetData()
        listener()
    }

    private fun listener() {
        binding.ivDeleteAttachmentAddExamQuestion.setOnClickListener {
            // If the delete button is pressed, the image will be deleted
            binding.ivAttachmentAddExamQuestion.visibility = View.GONE
            binding.ivDeleteAttachmentAddExamQuestion.visibility = View.GONE
            binding.ivAttachmentAddExamQuestion.setImageDrawable(null)
            pickedAttachmentImage = null
        }
        binding.viewPickAttachmentImageAddExamQuestion.setOnClickListener {
            // If the pick image button is pressed, the permission will be checked
            checkAndRequestPermission()
        }
        binding.ivIconPickAttachmentImageAddExamQuestion.setOnClickListener {
            // If the pick image button is pressed, the permission will be checked
            checkAndRequestPermission()
        }
        binding.btAttachmentAddExamQuestion.setOnClickListener {
            // If the add attachment button is pressed, the attachment will be added
            addAttachment()
        }
        binding.btOptionAddExamQuestion.setOnClickListener {
            // If the add option button is pressed, the option will be added
            addOption()
        }
        binding.btBottomSheetAddExamQuestion.setOnClickListener {
            // If the send button is pressed, the data will be sent to the parent activity
            if (!isSendingData) {
                checkInputs()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun resetData() {
        // Resetting the data
        attachments = mutableListOf()
        options = mutableListOf()
        pickedAttachmentImage = null

        binding.etAttachmentAddExamQuestion.setText("")
        binding.etOptionAddExamQuestion.setText("")
        binding.etQuestionAddExamQuestion.setText("")
        binding.etLayoutQuestionAddExamQuestion.error = null

        binding.ivAttachmentAddExamQuestion.setImageDrawable(null)

        binding.cbOptionAddExamQuestion.isChecked = false

        binding.tvAttachmentImageAddExamQuestion.visibility = View.VISIBLE
        binding.tvOptionAddExamQuestion.visibility = View.VISIBLE

        binding.recyclerViewOptionsAddExamQuestion.visibility = View.INVISIBLE
        binding.recyclerViewAttachmentAddExamQuestion.visibility = View.INVISIBLE

        binding.ivAttachmentAddExamQuestion.visibility = View.GONE
        binding.ivDeleteAttachmentAddExamQuestion.visibility = View.GONE

        isSendingData = false
    }

    private fun initAddAttachmentRecycler(data: MutableList<AddQuestionAttachment>) {
        // Initialize the recycler view
        addAttachmentAdapter = AddExamQuestionAttachmentAdapter(data, this)
        binding.recyclerViewAttachmentAddExamQuestion.adapter = addAttachmentAdapter
        binding.recyclerViewAttachmentAddExamQuestion.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun initAddOptionRecycler(data: MutableList<Option>) {
        // Initialize the recycler view
        addOptionAdapter = AddExamQuestionOptionAdapter(data, this)
        binding.recyclerViewOptionsAddExamQuestion.adapter = addOptionAdapter
        binding.recyclerViewOptionsAddExamQuestion.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun pickImageSetting() {
        // Register for Activity Results in onCreate()
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // Handle the result
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri: Uri? = result.data?.data

                    // Handle the selected image
                    uri?.let {
                        binding.ivAttachmentAddExamQuestion.visibility = View.VISIBLE
                        binding.ivDeleteAttachmentAddExamQuestion.visibility = View.VISIBLE
                        binding.ivAttachmentAddExamQuestion.setImageURI(it)
                        pickedAttachmentImage = it
                    } ?: run {
                        makeShortToast(requireContext(), "تصویری انتخاب نشده است")
                    }
                }
            }

        // Request permission launcher
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, launch the gallery
                pickImageLauncher.launch(getIntentForGallery())
            } else {
                // Handle permission denial
                makeShortToast(requireContext(), "مجوز دسترسی به گالری داده نشده است")
            }
        }
    }

    // Request permission
    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted. Launch the gallery.
            pickImageLauncher.launch(getIntentForGallery())
        } else {
            // Request the permission
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    // Get intent for gallery
    private fun getIntentForGallery(): Intent {
        return Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
    }

    // Add attachment
    private fun addAttachment() {
        // Check if the image is selected
        if (pickedAttachmentImage != null) {
            // Convert the Uri to a File object
            val file = getFileFromUri(requireContext(), pickedAttachmentImage!!)
            if (file != null) {
                // Add the attachment to the list
                val attachment =
                    AddQuestionAttachment(file, binding.etAttachmentAddExamQuestion.text.toString())

                binding.recyclerViewAttachmentAddExamQuestion.visibility = View.VISIBLE
                binding.tvAttachmentImageAddExamQuestion.visibility = View.GONE

                attachments.add(attachment)
                initAddAttachmentRecycler(attachments)
            } else {
                // Handle the case where the file is null
                makeShortToast(requireContext(), "فایل تصویر نامعتبر است")
            }
        } else {
            // Handle the case where the image is not selected
            makeShortToast(requireContext(), "لطفا تصویر را انتخاب کنید")
        }
    }

    // Add option
    private fun addOption() {
        // Check if the option is not empty
        if (binding.etOptionAddExamQuestion.text.toString().isNotEmpty()) {
            // Add the option to the list
            val option = Option(
                isCorrect = binding.cbOptionAddExamQuestion.isChecked,
                option = binding.etOptionAddExamQuestion.text.toString()
            )

            binding.recyclerViewOptionsAddExamQuestion.visibility = View.VISIBLE
            binding.tvOptionAddExamQuestion.visibility = View.GONE

            options.add(option)
            initAddOptionRecycler(options)
        } else {
            // Handle the case where the option is empty
            makeShortToast(requireContext(), "لطفا گزینه سوال را وارد کنید")
        }
    }

    // Check inputs
    private fun checkInputs() {
        val questionTitle = binding.etQuestionAddExamQuestion.text.toString()

        // Check if question title is empty
        if (questionTitle.isEmpty()) {
            binding.etLayoutQuestionAddExamQuestion.error = "لطفا صورت سوال را وارد کنید"
            makeShortToast(requireContext(), "لطفا صورت سوال را وارد کنید")
        } else {
            binding.etLayoutQuestionAddExamQuestion.isErrorEnabled = false

            // Check if options are empty
            if (options.isEmpty()) {
                makeShortToast(requireContext(), "لطفا گزینه های سوال را وارد کنید")
            } else {
                // Check if at least two options are selected
                if (options.size < 2) {
                    makeShortToast(requireContext(), "حداقل دو گزینه وارد کنید")
                } else {
                    if (options.any { it.isCorrect } && options.count { it.isCorrect } == 1) {
                        sendQuestionToParent(questionTitle)
                    } else {
                        makeShortToast(requireContext(), "فقط یک گزینه درست وارد کنید")
                    }
                }
            }
        }
    }

    // Send question to parent
    private fun sendQuestionToParent(questionTitle: String) {
        isSendingData = true

        // Convert options to a list of Option objects
        val attachmentList: List<AddQuestionAttachment> =
            attachments.map { AddQuestionAttachment(it.image, it.description) }

        // Create a Question object
        val question = AddQuestion(attachmentList, options, questionTitle)

        // Send question to the parent activity
        resetData()
        (requireActivity() as AddExamQuestionActivity).getBottomSheetData(question)
        dismiss()
    }

    // Delete attachment
    override fun onDeleteAttachmentClick(attachment: AddQuestionAttachment, position: Int) {
        attachments.remove(attachment)
        initAddAttachmentRecycler(attachments)

        if (attachments.isEmpty()) {
            binding.recyclerViewAttachmentAddExamQuestion.visibility = View.INVISIBLE
            binding.tvAttachmentImageAddExamQuestion.visibility = View.VISIBLE
        }
    }

    // Delete option
    override fun onDeleteOptionClick(option: Option, position: Int) {
        options.remove(option)
        initAddOptionRecycler(options)

        if (options.isEmpty()) {
            binding.recyclerViewOptionsAddExamQuestion.visibility = View.INVISIBLE
            binding.tvOptionAddExamQuestion.visibility = View.VISIBLE
        }
    }
}