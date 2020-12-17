package com.apolis.dec14selfie.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.apolis.dec14selfie.BuildConfig
import com.apolis.dec14selfie.R
import com.apolis.dec14selfie.data.models.Image
import com.apolis.dec14selfie.helpers.d
import com.apolis.dec14selfie.ui.auth.LoginActivity
import com.apolis.dec14selfie.ui.auth.RegisterActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.nav_header.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener,MainListener {

    lateinit var selfieViewModel:SelfieViewModel
    lateinit var selfieAdapter: SelfieAdapter
     var mStorageRef: StorageReference? = null
    var imageUri:Uri?=null
    var imageFilePath:String?=null
    var isFABOpen=false




    private val REQUEST_CAMERA_CODE = 100
    private val REQUEST_LOAD_IMAGE=200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        selfieViewModel=ViewModelProvider(this).get(SelfieViewModel::class.java)
        selfieViewModel.mainListener=this
        selfieViewModel.getImagesList()

        mStorageRef = FirebaseStorage.getInstance().getReference();
        init()

    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun init() {
        setupToolbar()
        setupNavi()
        setFAB()

        selfieAdapter=SelfieAdapter(this)
        main_recycler_view.adapter=selfieAdapter
        main_recycler_view.layoutManager=
            GridLayoutManager(this, 2)


        camara_button.setOnClickListener(this)
        gallery_button.setOnClickListener(this)

    }
    private fun setFAB(){
        fab.setOnClickListener {
            if(!isFABOpen)
            {
                showFABMenu()
            }
            else
            {
                closeFABMenu()
            }
        }
    }
    private fun showFABMenu() {
        isFABOpen = true
        fab.animate().rotation(45F)
        camara_button.animate().translationX(-200F)
        gallery_button.animate().translationX(200F)


    }

    private fun closeFABMenu() {
        isFABOpen = false
        fab.animate().rotation(0F)
        camara_button.animate().translationX(0F)
        gallery_button.animate().translationX(0F)

    }

    private fun setupToolbar() {


        var toolbar = toolbar
        toolbar.title = "Selfie App"
        toolbar.subtitle = "This is Selfie App"
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        var user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            menu?.findItem(R.id.item_Login)?.isVisible = false
            menu?.findItem(R.id.item_register)?.isVisible = false
            menu?.findItem(R.id.item_Logout)?.isVisible = true

        } else {
            menu?.findItem(R.id.item_Login)?.isVisible = true
            menu?.findItem(R.id.item_register)?.isVisible = true
            menu?.findItem(R.id.item_Logout)?.isVisible = false
        }
        return true

    }


    override fun onClick(v: View?) {
        when (v) {
            camara_button -> {
                requestCameraAndWritePermission()
            }

            gallery_button -> {
                requestReadPermission()
            }

        }
    }


    private fun requestReadPermission() {
        Dexter
            .withContext(this)
            .withPermission(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    //permission is granted
                    Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT)
                        .show()
                   openGallery()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                }

            }).check()
    }
    private fun requestCameraAndWritePermission() {
        Dexter.withContext(
            this
        ).withPermissions(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {

                    openCamera()
                }
                if (report.isAnyPermissionPermanentlyDenied) {
                    showDialogue()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }

        }).onSameThread()
            .check()

    }

    private fun openCamera() {
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val imageFile =createImageFile()
        imageUri= FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID+".provider",imageFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        startActivityForResult(intent, REQUEST_CAMERA_CODE)

    }

    private fun createImageFile():File{
//        val timestamp=System.currentTimeMillis().toString()
//        val storageDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val image=File.createTempFile(timestamp,".jpg",storageDir)
//         imageFilePath=image.absolutePath
//        return image
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            imageFilePath = absolutePath
            Log.d("123",imageFilePath.toString())
        }

    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==RESULT_OK&&data!=null){
            when(requestCode){
                REQUEST_LOAD_IMAGE->{
                    this.d("load image")
                   var selectImage=data!!.data
                    this.d("uri: ${selectImage.toString()}")
//                    Picasso.get().load(selectImage)
//                        .into(pre_view)


                    uploadData(selectImage!!)



                }
                REQUEST_CAMERA_CODE->{
//                    var photo=data.extras!!.get(MediaStore.EXTRA_OUTPUT) as Bitmap
                    this.d(imageUri.toString())
//                    Picasso.get().load(imageUri)
//                        .into(pre_view)
                    saveToGallery()

                    uploadData(imageUri!!)

//                    this.d("phone: ${photo.toString()}")
//                    pre_view.setImageBitmap(photo)
                }



            }

        }

    }
    private fun openGallery(){
        var photoPickerIntent=Intent(Intent.ACTION_PICK)
        photoPickerIntent.setType("image/*")
        this.d("openGallery")
        startActivityForResult(photoPickerIntent,REQUEST_LOAD_IMAGE)

    }

    private fun saveToGallery(){

        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(imageFilePath!!)
            this.d("scan data")
            this.d(imageFilePath.toString())
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }



    }

//    private fun uploadImage(selectedImage:Uri?){
//        if(selectedImage==null){
//            return
//        }
//        var ref=mStorageRef!!.
//        child("images/"+UUID.randomUUID().toString())
//        ref.putFile(selectedImage).addOnSuccessListener {
//            this.toast("upload image Success")
//
//        }
//            .addOnFailureListener{
//                this.toast("Failed: ${it.toString()}")
//                this.d("Failed: ${it.toString()}")
//            }
//
//
//    }



    private fun showDialogue() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Need Permission")
        builder.setMessage("Please please please please give the permission")
        builder.setPositiveButton("Go to Setting", object : DialogInterface.OnClickListener {
            override fun onClick(dialogue: DialogInterface?, p1: Int) {
                dialogue?.dismiss()
                openAppSettings()
            }
        })
        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(dialogue: DialogInterface?, p1: Int) {
                dialogue?.dismiss()
            }

        }).show()
    }


    private fun openAppSettings() {
        var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        var uri = Uri.fromParts("package", packageName, null)
        intent.setData(uri)
        startActivityForResult(intent, 101)
    }

    private fun setupNavi() {
        var drawLayout = drawer_layout
        var navView = nav_view
        var nav_menu = navView.menu
        var headView = navView.getHeaderView(0)
        var user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            nav_menu.findItem(R.id.item_Login).isVisible = false
            nav_menu.findItem(R.id.item_register).isVisible = false
            nav_menu.findItem(R.id.item_Logout).isVisible = true

            headView.text_view_header_name.text = user.displayName
            headView.text_view_header_email.isVisible = true
            headView.text_view_header_email.text = user.email
        } else {
            nav_menu.findItem(R.id.item_Login).isVisible = true
            nav_menu.findItem(R.id.item_register).isVisible = true
            nav_menu.findItem(R.id.item_Logout).isVisible = false

            headView.text_view_header_name.text = "Guest"
            headView.text_view_header_email.isVisible = false

        }
        var toggle = ActionBarDrawerToggle(this, drawLayout, toolbar, 0, 0)
        drawLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.item_Login -> {
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.item_register -> {
                Log.d("abc", "sign up")
                var intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)

            }
            R.id.item_Logout->{
                FirebaseAuth.getInstance().signOut()
            }
        }

        return true
    }

    override fun onGet(mList:LiveData<ArrayList<Image>>)  {
        mList.observe(this, androidx.lifecycle.Observer {
            selfieAdapter.setData(it)
//            for(i in it)
//            {
//                this.d("observe image:${i.location}+ ${i.uri}")
//            }
        })
    }

    override fun uploadData(uri: Uri) {
        selfieViewModel.uploadImage(uri)
    }
}