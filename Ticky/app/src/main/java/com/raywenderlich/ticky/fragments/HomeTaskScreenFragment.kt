package com.raywenderlich.ticky.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.ticky.MySharedPreference
import com.raywenderlich.ticky.R
import com.raywenderlich.ticky.Taskie
import com.raywenderlich.ticky.db.TaskieDatabase
import com.raywenderlich.ticky.db.dao.TaskieDao
import com.raywenderlich.ticky.repository.Factory
import com.raywenderlich.ticky.repository.TaskViewModel
import com.raywenderlich.ticky.repository.TaskieRepository
import com.raywenderlich.ticky.taskrecyclerview.SelectedTaskAdapter
import com.raywenderlich.ticky.taskrecyclerview.TodoListAdapter
import com.raywenderlich.ticky.taskrecyclerview.viewTypeAdapter
import kotlinx.android.synthetic.main.home_task_screen.*
import kotlinx.android.synthetic.main.home_task_screen.view.*
import kotlinx.android.synthetic.main.todo_list_view_holder.*
import kotlinx.android.synthetic.main.todo_list_view_holder.view.*
import java.util.*
import kotlin.collections.ArrayList


class HomeTaskScreenFragment : Fragment(), TodoListAdapter.Iunselect,
    SelectedTaskAdapter.unSelectListener {

    private var List = ArrayList<Taskie>()
    private var checkedList = ArrayList<Taskie>()
    private var selectedList = ArrayList<Taskie>()
    private var sortBy: String = ""

    private lateinit var selectedTaskRecyclerView: RecyclerView
    private lateinit var recyclerView: RecyclerView
    var adapter: TodoListAdapter = TodoListAdapter(click = { list, itemView, position ->
        okey(
            list,
            itemView,
            position
        )
    }, updateTask = { task -> checkTask(task) })

    var viewTypeAdapter : viewTypeAdapter = viewTypeAdapter(click = { list, itemView, position ->
        okey(
            list,
            itemView,
            position
        )
    })

    var selectedAdapter: SelectedTaskAdapter = SelectedTaskAdapter(unCheck = { task -> unCheck(task) })
    private lateinit var mTaskViewModel: TaskViewModel
    private lateinit var factory: Factory
    private lateinit var taskieDao: TaskieDao
    private lateinit var taskDB: TaskieDatabase
    private lateinit var repository: TaskieRepository
    private lateinit var mySharedPref: MySharedPreference
    private lateinit var viewHolder: TodoListAdapter.TodoListViewHolder


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.home_task_screen, container, false)



        initViewModel(view.context)




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDatee()

        val heroImageView = view.recycle
        ViewCompat.setTransitionName(heroImageView, "hero_image")

        val ttb = AnimationUtils.loadAnimation(context, R.anim.ttb)
        val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in2)
        val headerTitle = vnaxotraiqneba11 as ConstraintLayout
        val recycler = recycle as RecyclerView
      //  val recycler1 = checked_recycler as RecyclerView
        val textView = textView7 as TextView
        val imageViewer = imageView5 as ImageView
//        val textView2 = textView8 as TextView
//        val imageViewer2 = imageView6 as ImageView;

        textView.startAnimation(fadeIn)
        imageViewer.startAnimation(fadeIn)
      //  imageViewer2.startAnimation(fadeIn)
        // textView2.startAnimation(fadeIn)
        headerTitle.startAnimation(ttb)
        recycler.startAnimation(fadeIn)
      //  recycler1.startAnimation(fadeIn)


        adapter.setCheckedTaskListener(this)



        selectedAdapter.setOnCheckListener(this)

//        selectedTaskRecyclerView = view.checked_recycler
//        selectedTaskRecyclerView.adapter = selectedAdapter
//        selectedTaskRecyclerView.layoutManager = LinearLayoutManager(context)


        recyclerView = view.recycle
        recyclerView.adapter = viewTypeAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        mTaskViewModel.getTaskList().observe(viewLifecycleOwner, Observer { user ->


            viewTypeAdapter.setData(user)

        })

//        mTaskViewModel.getUnSelectedData().observe(viewLifecycleOwner, Observer { user ->
//            selectedAdapter.setSelectedData(user)
//        })


        add_task.setOnClickListener {
            listener?.homeTaskScrenButton()
        }




//
//        cancel_selecting.setOnClickListener {
//
//            List.clear()
//
//            mTaskViewModel.getTaskList().observe(viewLifecycleOwner, Observer { user ->
//                viewTypeAdapter.setData(user)
//            })
//            mTaskViewModel.getUnSelectedData().observe(viewLifecycleOwner, Observer { user ->
//                selectedAdapter.setSelectedData(user)
//            })
//            hideDeleteDonebttns()
//        }
//
//        done_button.setOnClickListener {
//            List.forEach { task ->
//                task.viewType = 2
//                task.checked = false
//
//            //    mTaskViewModel.updateTask(task)
//
//                viewTypeAdapter.notifyDataSetChanged()
//
//
//
//            }
//            List.clear()
//            hideDeleteDonebttns()
//        }
//


        view.textView5.setOnClickListener {
            var data = showDialog()
        }
        setSortingName()
    }



    private fun setSortingName() {
        val sortBy = mySharedPref.taskExe()
        if (sortBy != "") {
            view?.textView5?.text = sortBy
        }
    }


    private fun showDialog() {
        val dialog = CustomDialogFragment()




        dialog.show(childFragmentManager, "CustomDialog")
    }

    private fun initViewModel(context: Context) {
        taskDB = TaskieDatabase.getDatabase(context)
        taskieDao = taskDB.taskieDao()
        repository = TaskieRepository(taskieDao)
        factory = Factory(repository)
        mTaskViewModel = ViewModelProviders.of(this, factory).get(TaskViewModel::class.java)
        mySharedPref = MySharedPreference(context)
    }

    companion object {
        fun getHomeTaskScrenFragment(): HomeTaskScreenFragment {
            return HomeTaskScreenFragment()
        }
    }

    private fun setDatee() {
        val c = Calendar.getInstance()
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)


        val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
        val dayOfMonth = c.get(Calendar.DAY_OF_MONTH)

        monday1.text = (dayOfMonth - 2).toString()
        tuesday1.text = (dayOfMonth - 1).toString()
        wednesday1.text = dayOfMonth.toString()
        thursday1.text = (dayOfMonth + 1).toString()
        friday1.text = (dayOfMonth + 2).toString()
        saturday1.text = (dayOfMonth + 3).toString()
        sunday1.text = (dayOfMonth + 4).toString()
        // 0- jan , 1 - feb , 2 march , 3 april , 4 may , 5 june , 6 jule , 7 , august , 8 sept , 9 octo , 10 nov , 11 dec

        if (dayOfMonth == 1 && month == 0) {
            monday1.text = "30"
            tuesday1.text = "31"
        } else if (dayOfMonth == 1 && month == 2) {
            monday1.text = "27"
            tuesday1.text = "28"
        } else if (dayOfMonth == 1 && month == 4) {
            monday1.text = "29"
            tuesday1.text = "30"
        } else if (dayOfMonth == 1 && month == 6) {
            monday1.text = "29"
            tuesday1.text = "30"
        } else if (dayOfMonth == 1 && month == 7) {
            monday1.text = "30"
            tuesday1.text = "31"
        } else if (dayOfMonth == 1 && month == 9) {
            monday1.text = "29"
            tuesday1.text = "30"
        } else if (dayOfMonth == 1 && month == 11) {
            monday1.text = "29"
            tuesday1.text = "30"
        } else if (dayOfMonth == 2 && month == 0) {
            monday1.text = "31"
            tuesday1.text = "1"
        }
        // 0- jan 31 , 1 - feb 28 , 2 march 31 , 3 april 30 , 4 may 31  , 5 june 30 , 6 jule 31 , 7 august 31 , 8 sept 30 , 9 octo 31 , 10 nov 30, 11 dec 31
        else if (dayOfMonth == 2 && month == 2) {
            monday1.text = "28"
            tuesday1.text = "1"
        } else if (dayOfMonth == 2 && month == 4) {
            monday1.text = "30"
            tuesday1.text = "1"
        } else if (dayOfMonth == 2 && month == 6) {
            monday1.text = "30"
            tuesday1.text = "1"
        } else if (dayOfMonth == 2 && month == 7) {
            monday1.text = "31"
            tuesday1.text = "1"
        } else if (dayOfMonth == 2 && month == 9) {
            monday1.text = "30"
            tuesday1.text = "1"
        } else if (dayOfMonth == 2 && month == 11) {
            monday1.text = "30"
            tuesday1.text = "1"
        }
        // 0- jan 31 , 1 - feb 28 , 2 march 31 , 3 april 30 , 4 may 31  , 5 june 30 , 6 jule 31 , 7 august 31 , 8 sept 30 , 9 octo 31 , 10 nov 30, 11 dec 31

        else if (dayOfMonth == 1 && month == 1) {
            tuesday1.text = "31"
            monday1.text = "30"
        } else if (dayOfMonth == 2 && month == 1) {
            tuesday1.text = "1"
            monday1.text = "31"
        } else if (dayOfMonth == 1 && month == 3) {
            tuesday1.text = "31"
            monday1.text = "30"
        } else if (dayOfMonth == 1 && month == 5) {
            tuesday1.text = "31"
            monday1.text = "30"
        } else if (dayOfMonth == 1 && month == 8) {
            tuesday1.text = "31"
            monday1.text = "30"
        } else if (dayOfMonth == 1 && month == 10) {
            tuesday1.text = "31"
            monday1.text = "30"
        } else if (dayOfMonth == 2 && month == 3) {
            tuesday1.text = "1"
            monday1.text = "31"
        } else if (dayOfMonth == 2 && month == 5) {
            tuesday1.text = "1"
            monday1.text = "31"
        } else if (dayOfMonth == 2 && month == 8) {
            tuesday1.text = "1"
            monday1.text = "31"
        } else if (dayOfMonth == 2 && month == 10) {
            tuesday1.text = "1"
            monday1.text = "31"
        }
        // 0- jan 31 , 1 - feb 28 , 2 march 31 , 3 april 30 , 4 may 31  , 5 june 30 , 6 jule 31 , 7 august 31 , 8 sept 30 , 9 octo 31 , 10 nov 30, 11 dec 31

        else if (dayOfMonth == 28 && month == 0) {
            sunday1.text = "1"
        } else if (dayOfMonth == 28 && month == 2) {
            sunday1.text = "1"
        } else if (dayOfMonth == 28 && month == 4) {
            sunday1.text = "1"
        } else if (dayOfMonth == 28 && month == 6) {
            sunday1.text = "1"
        } else if (dayOfMonth == 28 && month == 7) {
            sunday1.text = "1"
        } else if (dayOfMonth == 28 && month == 9) {
            sunday1.text = "1"
        } else if (dayOfMonth == 28 && month == 11) {
            sunday1.text = "1"
        }
        // 0- jan 31 , 1 - feb 28 , 2 march 31 , 3 april 30 , 4 may 31  , 5 june 30 , 6 jule 31 , 7 august 31 , 8 sept 30 , 9 octo 31 , 10 nov 30, 11 dec 31

        else if (dayOfMonth == 29 && month == 0) {
            sunday1.text = "2"
            saturday1.text = "1"
        } else if (dayOfMonth == 29 && month == 2) {
            sunday1.text = "2"
            saturday1.text = "1"
        } else if (dayOfMonth == 29 && month == 4) {
            sunday1.text = "2"
            saturday1.text = "1"
        } else if (dayOfMonth == 29 && month == 6) {
            sunday1.text = "2"
            saturday1.text = "1"
        } else if (dayOfMonth == 29 && month == 7) {
            sunday1.text = "2"
            saturday1.text = "1"
        } else if (dayOfMonth == 29 && month == 9) {
            sunday1.text = "2"
            saturday1.text = "1"
        } else if (dayOfMonth == 29 && month == 11) {
            sunday1.text = "2"
            saturday1.text = "1"
        } else if (dayOfMonth == 30 && month == 0) {

            sunday1.text = "3"
            saturday1.text = "2"
            friday1.text = "1"
        } else if (dayOfMonth == 30 && month == 2) {

            sunday1.text = "3"
            saturday1.text = "2"
            friday1.text = "1"
        } else if (dayOfMonth == 30 && month == 4) {

            sunday1.text = "3"
            saturday1.text = "2"
            friday1.text = "1"
        } else if (dayOfMonth == 30 && month == 6) {

            sunday1.text = "3"
            saturday1.text = "2"
            friday1.text = "1"
        } else if (dayOfMonth == 30 && month == 7) {
            sunday1.text = "3"
            saturday1.text = "2"
            friday1.text = "1"
        } else if (dayOfMonth == 30 && month == 9) {
            sunday1.text = "3"
            saturday1.text = "2"
            friday1.text = "1"
        } else if (dayOfMonth == 30 && month == 11) {
            sunday1.text = "3"
            saturday1.text = "2"
            friday1.text = "1"
        } else if (dayOfMonth == 31 && month == 0) {
            sunday1.text = "4"
            saturday1.text = "3"
            friday1.text = "2"
            thursday1.text = "1"
        } else if (dayOfMonth == 31 && month == 2) {
            sunday1.text = "4"
            saturday1.text = "3"
            friday1.text = "2"
            thursday1.text = "1"
        } else if (dayOfMonth == 31 && month == 4) {
            sunday1.text = "4"
            saturday1.text = "3"
            friday1.text = "2"
            thursday1.text = "1"
        } else if (dayOfMonth == 31 && month == 6) {
            sunday1.text = "4"
            saturday1.text = "3"
            friday1.text = "2"
            thursday1.text = "1"
        } else if (dayOfMonth == 31 && month == 7) {
            sunday1.text = "4"
            saturday1.text = "3"
            friday1.text = "2"
            thursday1.text = "1"
        } else if (dayOfMonth == 31 && month == 9) {
            sunday1.text = "4"
            saturday1.text = "3"
            friday1.text = "2"
            thursday1.text = "1"
        } else if (dayOfMonth == 31 && month == 11) {
            sunday1.text = "4"
            saturday1.text = "3"
            friday1.text = "2"
            thursday1.text = "1"
        }
        // 0- jan 31 , 1 - feb 28 , 2 march 31 , 3 april 30 , 4 may 31  , 5 june 30 , 6 jule 31 , 7 august 31 , 8 sept 30 , 9 octo 31 , 10 nov 30, 11 dec 31

        else if (dayOfMonth == 27 && month == 3) {
            sunday1.text = "1"
        } else if (dayOfMonth == 27 && month == 5) {
            sunday1.text = "1"
        } else if (dayOfMonth == 27 && month == 8) {
            sunday1.text = "1"
        } else if (dayOfMonth == 27 && month == 10) {
            sunday1.text = "1"
        } else if (dayOfMonth == 28 && month == 3) {
            sunday1.text = "2"
            saturday1.text = "1"
        } else if (dayOfMonth == 28 && month == 5) {
            sunday1.text = "2"
            saturday1.text = "1"
        } else if (dayOfMonth == 28 && month == 8) {
            sunday1.text = "2"
            saturday1.text = "1"
        } else if (dayOfMonth == 28 && month == 10) {
            sunday1.text = "2"
            saturday1.text = "1"
        } else if (dayOfMonth == 29 && month == 3) {
            sunday1.text = "3"
            saturday1.text = "2"
            friday1.text = "1"
        } else if (dayOfMonth == 29 && month == 5) {
            sunday1.text = "3"
            saturday1.text = "2"
            friday1.text = "1"
        } else if (dayOfMonth == 29 && month == 8) {
            sunday1.text = "3"
            saturday1.text = "2"
            friday1.text = "1"
        } else if (dayOfMonth == 29 && month == 10) {
            sunday1.text = "3"
            saturday1.text = "2"
            friday1.text = "1"
        } else if (dayOfMonth == 30 && month == 3) {
            sunday1.text = "4"
            saturday1.text = "3"
            friday1.text = "2"
            thursday1.text = "1"
        } else if (dayOfMonth == 30 && month == 5) {
            sunday1.text = "4"
            saturday1.text = "3"
            friday1.text = "2"
            thursday1.text = "1"
        } else if (dayOfMonth == 30 && month == 8) {
            sunday1.text = "4"
            saturday1.text = "3"
            friday1.text = "2"
            thursday1.text = "1"
        } else if (dayOfMonth == 30 && month == 10) {
            sunday1.text = "4"
            saturday1.text = "3"
            friday1.text = "2"
            thursday1.text = "1"
        } else if (dayOfMonth == 28 && month == 1) {
            sunday1.text = "4"
            saturday1.text = "3"
            friday1.text = "2"
            thursday1.text = "1"
        }
        if (dayOfWeek == 0) {
            Monday1.text = "T"
            monday21.text = "F"
            monday41.text = "S"
            monday51.text = "S"
            monday61.text = "M"
            monday71.text = "T"
            monday81.text = "W"

        } else if (dayOfWeek == 1) {
            Monday1.text = "F"
            monday21.text = "S"
            monday41.text = "S"
            monday51.text = "M"
            monday61.text = "T"
            monday71.text = "W"
            monday81.text = "T"

        } else if (dayOfWeek == 2) {
            Monday1.text = "S"
            monday21.text = "S"
            monday41.text = "M"
            monday51.text = "T"
            monday61.text = "W"
            monday71.text = "T"
            monday81.text = "F"
        } else if (dayOfWeek == 3) {
            Monday1.text = "S"
            monday21.text = "M"
            monday41.text = "T"
            monday51.text = "W"
            monday61.text = "T"
            monday71.text = "F"
            monday81.text = "S"
        } else if (dayOfWeek == 4) {
            Monday1.text = "M"
            monday21.text = "T"
            monday41.text = "W"
            monday51.text = "T"
            monday61.text = "F"
            monday71.text = "S"
            monday81.text = "S"

        } else if (dayOfWeek == 5) {
            Monday1.text = "T"
            monday21.text = "W"
            monday41.text = "T"
            monday51.text = "F"
            monday61.text = "S"
            monday71.text = "S"
            monday81.text = "M"
        } else if (dayOfWeek == 6) {
            Monday1.text = "W"
            monday21.text = "T"
            monday41.text = "F"
            monday51.text = "S"
            monday61.text = "S"
            monday71.text = "M"
            monday81.text = "T"
        }
        if (month == 0) {
            datetime1.text = ("January, $year")
        }
        if (month == 1) {
            datetime1.text = ("February, $year")
        }
        if (month == 2) {
            datetime1.text = ("March, $year")
        }
        if (month == 3) {
            datetime1.text = ("April, $year")
        }
        if (month == 4) {
            datetime1.text = ("May, $year")
        }
        if (month == 5) {
            datetime1.text = ("June, $year")
        }
        if (month == 6) {
            datetime1.text = ("July, $year")
        }
        if (month == 7) {
            datetime1.text = ("August, $year")
        }
        if (month == 8) {
            datetime1.text = ("September, $year")
        }
        if (month == 9) {
            datetime1.text = ("October, $year")
        }
        if (month == 10) {
            datetime1.text = ("November, $year")
        }
        if (month == 11) {
            datetime1.text = ("December, $year")
        }
    }

    private var listener: HomeTaskScreenButton? = null
    private var listener1: deletingUser? = null

    interface deletingUser {
        fun deletingUser()
    }

    interface HomeTaskScreenButton {
        fun homeTaskScrenButton()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as HomeTaskScreenButton
//        listener1 = context as deletingUser
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        //    listener1 = null
    }

    private fun hideDeleteDonebttns() {
        val done_button_anim = AnimationUtils.loadAnimation(context, R.anim.done_button_anim_down)
        val delete_button_anim = AnimationUtils.loadAnimation(
            context,
            R.anim.delete_done_button_down
        )
        val x_button_anim = AnimationUtils.loadAnimation(context, R.anim.x_button_anim_down)

        done_button.startAnimation(done_button_anim)
        delete_task_button.startAnimation(delete_button_anim)
        cancel_selecting.startAnimation(x_button_anim)

        x_button_anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                selected_item_funcs.visibility = View.INVISIBLE


            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })


    }

    private fun showDeleteDonebtttns() {
        val done_button_anim = AnimationUtils.loadAnimation(context, R.anim.done_button_anim_up)
        val delete_button_anim = AnimationUtils.loadAnimation(context, R.anim.delete_button_anim_up)
        val x_button_anim = AnimationUtils.loadAnimation(context, R.anim.x_button_anim_up)

        done_button.startAnimation(done_button_anim)
        delete_task_button.startAnimation(delete_button_anim)
        cancel_selecting.startAnimation(x_button_anim)

        selected_item_funcs.visibility = View.VISIBLE

    }
    private fun checkTask(task: Taskie) {

        task.selected = true
        task.checked = false


//        val anim = ObjectAnimator.ofFloat(itemView , View.TRANSLATION_Y , 0f ,2000f )
//        if(position == 0){
//            anim.duration = 700
//        }else if (position == 1){
//            anim.duration = 650
//        }
//        else if (position == 2){
//            anim.duration = 600
//        }
//        else if(position == 3){
//            anim.duration = 500
//        }
//        else if (position == 4){
//            anim.duration = 400
//        }else {
//            anim.duration = 300
//        }
////        android:transitionName="@string/sharedchars"
//
//        anim.start()
//

      //  anim.doOnEnd {
            mTaskViewModel.updateTask(task)


     //   }

        hideDeleteDonebttns()


    }

    private fun okey(list: List<Taskie>, itemView: ArrayList<View>, position: ArrayList<Int>){
        this.List = list as ArrayList<Taskie>
        Log.e("beforeanim", "taskebis sia $list  , poziciebi $position")


        cancel_selecting.setOnClickListener {

            list.clear()
            itemView.clear()
            position.clear()
            mTaskViewModel.getTaskList().observe(viewLifecycleOwner, Observer { user ->
                viewTypeAdapter.setData(user)
            })
//            mTaskViewModel.getUnSelectedData().observe(viewLifecycleOwner, Observer { user ->
//                selectedAdapter.setSelectedData(user)
//            })
            hideDeleteDonebttns()
        }

        done_button.setOnClickListener {
            list.forEach { task ->
                task.viewType = 2
                task.checked = false

                //    mTaskViewModel.updateTask(task)

                viewTypeAdapter.notifyDataSetChanged()

            }
            List.clear()
            itemView.clear()
            position.clear()
            hideDeleteDonebttns()
        }



        delete_task_button.setOnClickListener {

                val anim = AnimationUtils.loadAnimation(context, R.anim.slide_out)
                itemView.forEach { v->
                    v.startAnimation(anim)
                }
                Log.e("onstartanim", "taskebis sia $list  , poziciebi $position")

                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        list.forEach { task ->
                            task.checked = false
                        }



                        hideDeleteDonebttns()
                    }

                    override fun onAnimationEnd(animation: Animation?) {

                        itemView.forEach { v ->
                            v.alpha = 0F
                        }

                        Log.e("onendanim", "taskebis sia $list  , poziciebi $position")
                        mTaskViewModel.deleteUser(list)


//                        position.forEach {
//                            viewTypeAdapter.notifyItemRemoved(it)
//                        }

                        position.forEach {

                            viewTypeAdapter.notifyItemRemoved(it)
//                            viewTypeAdapter.completedlist.removeAt(it)

//                            viewTypeAdapter.completedlist.removeAt(it - 1)


                        }


                        //  viewTypeAdapter.notifyDataSetChanged()
                        position.clear()
                        itemView.clear()
//                        list.clear()
                        Log.e("onclearanim", "taskebis sia $list  , poziciebi $position")
                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                })



            }

            if (list.isEmpty()) {
                hideDeleteDonebttns()

            } else if (selected_item_funcs.isVisible) {

            } else {
                showDeleteDonebtttns()
            }

//        done_button.setOnClickListener {
//            List.forEach { task ->
//                itemView.forEach { v->
//                    position.forEach {
//                        checkTask(task,v,it)
//                    }
//                }
//
//            }
//
//            List.clear()
//        }



    }



    override fun unSelect(list: List<Taskie>) {

        this.checkedList = list as ArrayList<Taskie>

        selectedAdapter.setSelectedData(checkedList)

    }

    override fun unSelectSelected(list: List<Taskie>) {

        this.selectedList = list as ArrayList<Taskie>

    }

//    override fun updateTask(task: Taskie, itemView: View) {
//
//        val anim = ObjectAnimator.ofFloat(itemView , View.TRANSLATION_Y , 0f , 100f )
//        anim.duration = 1000
//        anim.start()
//
//        mTaskViewModel.updateTask(task)
//        hideDeleteDonebttns()
//    }

    private fun unCheck(task: Taskie) {

//
//        val anim = ObjectAnimator.ofFloat(itemView , View.TRANSLATION_Y , 0f ,-2000f )
//        if(position == 0){
//            anim.duration = 700
//        }else if (position == 1){
//            anim.duration = 650
//        }
//        else if (position == 2){
//            anim.duration = 600
//        }
//        else if(position == 3){
//            anim.duration = 500
//        }
//        else if (position == 4){
//            anim.duration = 400
//        }else {
//            anim.duration = 300
//        }
////        android:transitionName="@string/sharedchars"
//
//        anim.start()


//
//            if (position == 0) {
//                selectedAdapter.notifyItemRemoved(position)
//                selectedAdapter.selectedList.removeAt(position)
//            } else {
//                adapter.notifyItemRemoved(position)
//                selectedAdapter.selectedList.removeAt(position - 1)
//            }



//        selectedAdapter.notifyItemRemoved(position)
//        selectedAdapter.selectedList.remove(task)
//

//        anim.doOnEnd {
            mTaskViewModel.updateTask(task)
       // }

        hideDeleteDonebttns()



    }


}