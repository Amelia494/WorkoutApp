package com.android.workoutappamelia

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat.format
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.String.format
import android.text.format.DateFormat
import android.view.*
import androidx.lifecycle.Observer
import java.text.MessageFormat.format
import java.util.*

private const val TAG = "WorkoutListFragment"

class WorkoutListFragment : Fragment() {

    interface Callbacks {
        fun onWorkoutSelected(workoutId: UUID)
    }
    private var callbacks: Callbacks? = null

    private lateinit var workoutRecyclerView: RecyclerView
    private var adapter: WorkoutAdapter? = WorkoutAdapter(emptyList())

    private val workoutListViewModel: WorkoutListViewModel by lazy {
        ViewModelProviders.of(this).get(WorkoutListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout_list, container, false)

        workoutRecyclerView = view.findViewById(R.id.workout_recycler_view) as RecyclerView
        workoutRecyclerView.layoutManager = LinearLayoutManager(context)
        workoutRecyclerView.adapter = adapter
        return view
    }
    private fun updateUI(workouts: List<Workout>) {
        adapter = WorkoutAdapter(workouts)
        workoutRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workoutListViewModel.workoutListLiveData.observe(
            viewLifecycleOwner,
            Observer { workouts ->
                workouts?.let {
                    Log.i(TAG, "Got workouts ${workouts.size}")
                    updateUI(workouts)
                }
            })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_workout_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_workout -> {
                val workout = Workout()
                workoutListViewModel.addWorkout(workout)
                callbacks?.onWorkoutSelected(workout.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private inner class WorkoutHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var workout: Workout

        private val titleTextView: TextView = itemView.findViewById(R.id.workout_title)
        private val locationTextView: TextView = itemView.findViewById(R.id.workout_location)
        private val dateTextView: TextView = itemView.findViewById(R.id.workout_date)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(workout: Workout){
            this.workout = workout
            titleTextView.text = this.workout.title
            locationTextView.text = this.workout.place
            dateTextView.text = DateFormat.format("EEEE, dd MMM yyyy", this.workout.date)
        }

        override fun onClick(v: View) {
            callbacks?.onWorkoutSelected(workout.id)
        }
    }

    private inner class WorkoutAdapter(var workouts: List<Workout>) :
        RecyclerView.Adapter<WorkoutHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
                WorkoutHolder {
            val view = layoutInflater.inflate(R.layout.list_item_workout, parent, false)
            return WorkoutHolder(view)
        }

        override fun onBindViewHolder(holder: WorkoutHolder, position: Int) {
            val workout = workouts[position]
            holder.bind(workout)
        }

        override fun getItemCount() = workouts.size
    }

    companion object {
        fun newInstance(): WorkoutListFragment {
            return WorkoutListFragment()
        }
    }
}