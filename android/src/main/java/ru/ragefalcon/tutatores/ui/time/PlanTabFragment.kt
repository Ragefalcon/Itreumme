package ru.ragefalcon.tutatores.ui.time

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import ru.ragefalcon.tutatores.R

class PlanTabFragment: Fragment()  {

    val fragment = PlanFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val fragment = FragmentB()
//        val supportFragmentManager = requireActivity().getSupportFragmentManager()
//        supportFragmentManager.commit {
//            setCustomAnimations(...)
//            addSharedElement(itemImageView, “hero_image”)
//            replace(R.id.fragment_container, fragment)
//            addToBackStack(null)
//        }
//        val fragmentManager: FragmentManager = requireActivity().getSupportFragmentManager()
//        requireActivity().supportFragmentManager.commit {
//            setReorderingAllowed(true)
//            replace(R.id.fragment_container, fragment)
//            addToBackStack(null)
//        }
//        fragmentManager
//            .beginTransaction()
//            .add(R.id.fragment_container, PlanFragment(), PlanFragment::class.java.getSimpleName())
//            .commit()

    }

    override fun onResume() {
        super.onResume()
//        requireActivity().supportFragmentManager.commit {
//            setReorderingAllowed(true)
//            replace(R.id.fragment_container, fragment)
//            addToBackStack(null)
//        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlanTabFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}