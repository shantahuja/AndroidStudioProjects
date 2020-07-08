package com.example.picturepicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import kotlinx.android.synthetic.main.activity_create_collection.*

class CreateCollection : AppCompatActivity(), View.OnClickListener {
    private val photoArrayList = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_collection)

        collectionName.doAfterTextChanged { text  ->
            if (text == null) createCollectionButton.isEnabled = false
           else  createCollectionButton.isEnabled = !(text.isEmpty())
        }

        lion.setOnClickListener(this)
        lionwithfeathers2.setOnClickListener(this)
        lionwithfeathers3.setOnClickListener(this)
        lionwithgeometry2.setOnClickListener(this)
        createCollectionButton.setOnClickListener {
            if (( photoArrayList.size > 0)) {
                val newCollection = Collection(collectionName.text.toString(), photoArrayList.map{
                    when (it) {
                        R.id.lionwithfeathers2 -> R.drawable.lionwithfeathers2
                        R.id.lionwithfeathers3 -> R.drawable.lionwithfeathers3
                        R.id.lionwithgeometry2 -> R.drawable.lionwithgeometry2
                        else ->  R.drawable.lion
                    }
                }.toIntArray())
                Database.database.add(newCollection)
                finish()
            }
        }
    }

    override fun onClick(p0: View?) {
        if (p0 == null) return;
        if (photoArrayList.contains(p0.id)) {
            photoArrayList.remove(p0.id)
        } else {
            photoArrayList.add(p0.id)
        }
        Toast.makeText(this, "In list ${(photoArrayList.contains(p0.id))}", Toast.LENGTH_SHORT).show()

        p0.setBackgroundResource(if (photoArrayList.contains(p0.id)) R.drawable.background else 0)


        if (photoArrayList.size > 0) {
            collectionName.visibility = View.VISIBLE
            createCollectionButton.visibility = View.VISIBLE
        } else {
            collectionName.visibility = View.INVISIBLE
            createCollectionButton.visibility = View.INVISIBLE
        }
        // if they selected something and it's not already in there
        // then add it
        // but if it's in there then take it out
        // if else statement, check if it's in there, then take it out
        // otherwise add it
        // second part is another if statement after this block
        // if photoarraylist size is greater than 0 then allow the create collection
        // and allow to name the collection

        // inside of the if statement that checks to see if it's already in there
        // if they tap on something and it's already in the array list, take off the border
        // if it's not, and it's already in the array list, then you need to remove the border

        // the way to add the border is to put the name of the image
        // you would need to use the name of the image
        // it would be whatever the image name is in the xml
        // lion.visible = true


        /*when (p0?.id) {
            R.drawable.lion -> photoArrayList.add(p0.id)
            R.drawable.lionwithfeathers2 -> photoArrayList.add(p0.id)
        }*/
    }
}