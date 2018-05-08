package barbapplications.sft

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home_screen.*



class HomeScreenActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        //new stuff
       val kBadgeNo= intent.getStringExtra("kBadgeNo")


        //new stuff


        btnOpenCases.setOnClickListener {
            val intent = Intent (this,ExistingCases:: class.java)
            intent.putExtra(kBadgeNo,kBadgeNo)
            startActivity(intent)


        }

        ////new stuff

        val CreateCase:Button = findViewById(R.id.btnCreateCase)
        CreateCase.setOnClickListener{
            val intent = Intent (this,NewCaseActivity:: class.java)
            startActivity(intent)

        }


    }

}

