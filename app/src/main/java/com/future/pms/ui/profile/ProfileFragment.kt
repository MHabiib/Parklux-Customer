package com.future.pms.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import com.future.pms.R
import com.future.pms.di.base.BaseMVPActivity
import com.future.pms.di.base.BaseMVPFragment
import com.future.pms.model.oauth.Token
import com.future.pms.util.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : BaseMVPFragment<ProfileContract.View, ProfileContract.Presenter>(), ProfileContract.View{

    override var presenter: ProfileContract.Presenter = ProfilePresenter()

    private lateinit var rootView: View

    fun newInstance(): ProfileFragment {
        return ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        val submit = rootView.findViewById(R.id.btnLogout) as Button
        submit.setOnClickListener {
            btnLogout.visibility = View.GONE

            presenter.signOut()
        }

       /* rootView.profile_name_display.text = Gson().fromJson(context?.getSharedPreferences(
            Constants.AUTHENTCATION,
            Context.MODE_PRIVATE
        )?.getString(Constants.TOKEN, null), Token::class.java).email*/
        return rootView
    }

    override fun showProgress(show: Boolean) {}

    override fun onLogout() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun injectDependency() {
        val profileComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()
        profileComponent.inject(this)
    }

    companion object {
        const val TAG: String = "ProfileFragment"
    }
}