package com.example.foodapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.example.foodapp.databinding.FragmentRecipeBinding
import java.io.ByteArrayOutputStream


class RecipeFragment : Fragment() {

    private lateinit var viewBinding : FragmentRecipeBinding

    var choosenImage  : Uri? = null
    var choosenBitmap : Bitmap? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentRecipeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.button.setOnClickListener{
            save(it)
        }
        viewBinding.imageView.setOnClickListener{
            choosen_image(it)
        }

    }

    fun save(view : View) {
        //sqLiteSave part
        val foodName      = viewBinding.foodNameText.text.toString()
        val foodMaterials = viewBinding.foodMaterialsText.text.toString()

        if(choosenBitmap != null) {

            val smallBitmap  = forSmallBitmap(choosenBitmap!!,300)
            val outputStream = ByteArrayOutputStream() //converting bitmap to data
            smallBitmap.compress(Bitmap.CompressFormat.PNG,40,outputStream)

            val byteArray    = outputStream.toByteArray()

            try {
                context?.let {
                    val database = it.openOrCreateDatabase("Recipe", Context.MODE_PRIVATE, null)
                    database.execSQL("CREATE TABLE IF NOT EXISTS recipe (id INTEGER PRIMARY KEY, foodName VARCHAR, foodMaterial VARCHAR, image BLOB)")

                    //database.execSQL("INSERT INTO recipe(foodName,foodMaterial,image) VALUES()")
                    val sqlString =  "INSERT INTO recipe(foodName, foodMaterial, image) VALUES(?, ?, ?)"
                    val statement = database.compileStatement(sqlString)
                    statement.bindString(1, foodName)
                    statement.bindString(2, foodMaterials)
                    statement.bindBlob(3,byteArray)

                    statement.execute() //calıstır


                }



            }catch (e:Exception) {

            }

            val action = RecipeFragmentDirections.actionRecipeFragmentToListFragment()
            Navigation.findNavController(view).navigate(action)
            
        }
    }

    fun choosen_image(view : View) {
        // user needs to give permission 1 time, check needs to be done first
        // We call it with ContextCompat because no matter which version is used in all APIs, there is no version incompatibility.
        // If we had called it from Activity, we would have said checkSelfPermission.
        activity?.let {
            if(ContextCompat.checkSelfPermission(it.applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // permission was not given, permission was requested
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                // Permission granted, go to gallery without asking again
                val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                // The uri tells us where the image we received is located.
                startActivityForResult(galleryIntent, 2)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode  : Int,
        permissions  : Array<out String>,
        grantResults : IntArray
    ) {
        if(requestCode ==1) {
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //izni aldık
                val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 2)
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(
        requestCode : Int,
        resultCode  : Int,
        data        : Intent?
    ) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

            choosenImage = data.data

            try {
                context?.let {
                    if (choosenImage != null) {
                        if( Build.VERSION.SDK_INT >=28) {
                            val source    = ImageDecoder.createSource(it.contentResolver,choosenImage!!)
                            choosenBitmap = ImageDecoder.decodeBitmap(source)
                            viewBinding.imageView.setImageBitmap(choosenBitmap)
                        } else {
                            choosenBitmap = MediaStore.Images.Media.getBitmap(it.contentResolver, choosenImage)
                            viewBinding.imageView.setImageBitmap(choosenBitmap)
                        }


                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun forSmallBitmap(userChoosenBitmap: Bitmap, maxSize:Int) : Bitmap {

        var width  = userChoosenBitmap.width
        var height = userChoosenBitmap.height

        val bitmapProportion : Double = width.toDouble() / height.toDouble()

        if(bitmapProportion >= 1) {
            // visual horizontal
            width = maxSize
            val shortenedHeight = width / bitmapProportion
            height = shortenedHeight.toInt()
        } else {
            // visual vertical
            height = maxSize
            val shortenedWidth = height * bitmapProportion
            width = shortenedWidth.toInt()
        }

        return Bitmap.createScaledBitmap(userChoosenBitmap,width,height,true)
    }

}