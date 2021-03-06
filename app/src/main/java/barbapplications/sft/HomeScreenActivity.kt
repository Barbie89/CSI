package barbapplications.sft

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home_screen.*



class HomeScreenActivity : AppCompatActivity() {
    private var badgeNo: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        badgeNo= intent.getStringExtra("kBadgeNo")
        btnOpenCases.setOnClickListener {
            val intent = Intent (this,ExistingCases:: class.java)
            intent.putExtra("kBadgeNo", badgeNo)
            startActivity(intent)


        }
        val CreateCase:Button = findViewById(R.id.btnCreateCase)
        CreateCase.setOnClickListener{
            val intent = Intent (this,NewCaseActivity:: class.java)
            startActivity(intent)

        }


    }
}

