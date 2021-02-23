package com.raywenderlich.ticky.taskrecyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.ticky.*
import com.raywenderlich.ticky.fragments.HomeTaskScreenFragment
import kotlinx.android.synthetic.main.home_task_screen.view.*
import kotlinx.android.synthetic.main.todo_list_view_holder.view.*
import java.util.*
import kotlin.collections.ArrayList


class TodoListAdapter : RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> () {

    private var taskList1 = ArrayList<Taskie>()
    private var taskList = ArrayList<Taskie>()
    private var checkedTaskList = ArrayList<Taskie>()
    inner class TodoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(task: Taskie , holder: TodoListViewHolder , position: Int) {
            itemView.setOnLongClickListener {


                if (!task.checked) {
                    task.checked = true
                    itemView.linearLayout.setBackgroundResource(R.drawable.selected_item)
                    itemView.checkBox.setButtonDrawable(R.drawable.ic_rectangle_completed)
                    taskList1.add(task)
                    listener?.onChecked(taskList1)



                } else {
                    task.checked = false
                    holder.itemView.checkBox.isChecked = false
                    itemView.linearLayout.setBackgroundResource(R.drawable.viewholder_background)
                    itemView.checkBox.setButtonDrawable(R.drawable.unselected_task_checkbox)
                    taskList1.remove(task)
                    listener?.onChecked(taskList1)

                }


                    true
                }


//            itemView.checkBox.setOnClickListener {
////
////                checkedTaskList.add(task)
////                taskList.remove(task)
////                listener1?.unSelect(checkedTaskList)
//                task.selected = true
//
//                notifyDataSetChanged()
//
//            }
            }

        fun checkTask(task: Taskie , holder: TodoListViewHolder ){
            itemView.checkBox.setOnClickListener {




                task.selected = true
                listener2?.updateTask(task)



                notifyDataSetChanged()
            }
        }






    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        return TodoListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.todo_list_view_holder, parent, false)
        )

    }

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {





        val currentItem = (taskList[position])



        holder.checkTask(currentItem , holder )

        holder.itemView.linearLayout.setBackgroundResource(R.drawable.viewholder_background)

        holder.itemView.checkBox.isChecked = false
        holder.setData(currentItem , holder , position)




        holder.itemView.task.text = currentItem.title


        if (currentItem.color == "#ff453a") {
            holder.itemView.task_color_red.visibility = View.VISIBLE
        } else if (currentItem.color == "#ff9f0c") {
            holder.itemView.task_color_orange.visibility = View.VISIBLE
        } else if (currentItem.color == "#ffd50c") {
            holder.itemView.task_color_yellow.visibility = View.VISIBLE
        } else if (currentItem.color == "#32d74b") {
            holder.itemView.task_color_green.visibility = View.VISIBLE
        } else if (currentItem.color == "#64d2ff") {
            holder.itemView.task_color_pachtiblue.visibility = View.VISIBLE
        } else if (currentItem.color == "#0984ff") {
            holder.itemView.task_color_blue.visibility = View.VISIBLE
        } else if (currentItem.color == "#5e5ce6") {
            holder.itemView.task_color_muqiblue.visibility = View.VISIBLE
        } else if (currentItem.color == "#bf5af2") {
            holder.itemView.task_color_purple.visibility = View.VISIBLE
        } else if (currentItem.color == "#ff375f") {
            holder.itemView.task_color_rose.visibility = View.VISIBLE
        } else {
            holder.itemView.task_color_red.visibility = View.INVISIBLE
            holder.itemView.task_color_orange.visibility = View.INVISIBLE
            holder.itemView.task_color_yellow.visibility = View.INVISIBLE
            holder.itemView.task_color_pachtiblue.visibility = View.INVISIBLE
            holder.itemView.task_color_blue.visibility = View.INVISIBLE
            holder.itemView.task_color_muqiblue.visibility = View.INVISIBLE
            holder.itemView.task_color_green.visibility = View.INVISIBLE
            holder.itemView.task_color_purple.visibility = View.INVISIBLE
            holder.itemView.task_color_rose.visibility = View.INVISIBLE

        }

        if (currentItem.datetime == "") {
            holder.itemView.task_date.visibility = View.GONE
        } else {
            holder.itemView.task_date.visibility = View.VISIBLE
            holder.itemView.task_date.text = currentItem.datetime
        }


        holder.setIsRecyclable(false)

    }

    override fun getItemCount(): Int = taskList.size

    override fun getItemId(position: Int): Long = position.toLong()




    fun setData(task: List<Taskie>) {
        this.taskList.clear()
        this.taskList.addAll(task)
        notifyDataSetChanged()
    }


    var listener22 : UpdateAllTasks? = null
    var listener2 : UpdateTask? = null

    interface UpdateAllTasks {
        fun updateAllTasks(list : ArrayList<Taskie>)
    }
    interface UpdateTask {
        fun updateTask(task: Taskie)
    }
    fun updateAllTasks(listener22 : UpdateAllTasks){
        this.listener22 = listener22
    }
    fun updateTask(listener2 : UpdateTask){
        this.listener2 = listener2
    }



    interface IOnClick {
        fun onChecked(list: List<Taskie>)
    }

    interface Iunselect {
        fun unSelect(list: List<Taskie>)
    }
    var listener: IOnClick? = null

    var listener1 : Iunselect? = null

    fun setOnCheckListener(listener: IOnClick) {
        this.listener = listener
    }
    fun setCheckedTaskListener(listener1 : Iunselect){
        this.listener1 = listener1
    }

}
//    override fun onItemMove(
//        recyclerView: RecyclerView,
//        fromPosition: Int,
//        toPosition: Int
//    ): Boolean {
//        if(fromPosition < toPosition)
//            for(i in fromPosition until toPosition){
//                Collections.swap(taskList, i , i +1)
//            }else {
//            for (i in fromPosition downTo toPosition + 1){
//                Collections.swap(taskList,i , i -1 )
//            }
//        }
//        notifyItemMoved(fromPosition , toPosition)
//        return true
//    }
