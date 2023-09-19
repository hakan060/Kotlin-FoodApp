package com.example.foodapp
// Fragment that helps us show the list of recipes
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private lateinit var viewBinding : FragmentListBinding
    var foodNameList = ArrayList<String>()
    var foodIdList   = ArrayList<Int>()
    private lateinit var listAdapter : ListRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentListBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ListRecyclerAdapter(foodNameList,foodIdList)
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.adapter = listAdapter

        sqlTakeData()
    }

    fun sqlTakeData() {
        try {
            context?.let {
                val database      = it.openOrCreateDatabase("Recipe", Context.MODE_PRIVATE, null)
                val cursor        = database.rawQuery("SELECT * FROM recipe", null)
                val foodNameIndex = cursor.getColumnIndex("foodName")
                val foodIdIndex   = cursor.getColumnIndex("id")

                foodNameList.clear()
                foodIdList.clear()

                while (cursor.moveToNext()) {
                    foodNameList.add(cursor.getString(foodNameIndex))
                    foodIdList.add(cursor.getInt(foodIdIndex))
                }
                listAdapter.notifyDataSetChanged() // data changed and new data update

                cursor.close()

            }
        } catch (e: Exception) {

        }
    }
}