package com.example.nego.Repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nego.Responses.LoginResponse
import com.example.nego.Responses.SignupFailure
import com.example.nego.Responses.SignupSuccess
import com.example.nego.Responses.User
import com.example.nego.Retrofit.RetrofitClient
import com.example.nego.Retrofit.loginUser
import com.example.nego.Retrofit.signupUser
import com.example.nego.adapter.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    var userList = ArrayList<User>()
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    data class FirebaseUserResult(val icon: String?, val username: String?,val uid: String?)
    fun  startLogin(email: String, password: String): LiveData<ResponseBody?> {

            val userlogin = loginUser(email, password);
            val loginResponse = MutableLiveData<ResponseBody>();
            Log.d(TAG, "startLogin: " + userlogin.toString());
            Log.d(TAG, "startLogin: " + email + password);
            RetrofitClient.apiService.login(userlogin)?.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    loginResponse.value = response.body();
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        // Process the response body here
                        if (responseBody != null) {
                            val responseString = responseBody.string()
                            val loginResponse_ = Gson().fromJson(
                                responseString,
                                LoginResponse.LoginResponseSuccess::class.java
                            )
                            // Handle the success response here
                            Log.d(TAG, "Signup Status: ${loginResponse_.message}")
                            val status = loginResponse_.success
                            val email = loginResponse_.existingUser.email
                            val name = loginResponse_.existingUser.name
                        }
                    } else {
                        val errorBody = response.errorBody()
                        loginResponse.value = response.body();
                        if (errorBody != null) {
                            val errorString = errorBody.string()
                            val loginFailure =
                                Gson().fromJson(errorString, LoginResponse.LoginFailed::class.java)
                            // Handle the failure response here
                            Log.d(TAG, "Signup Status: ${loginFailure.message}")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Log.d(TAG, "login Status:" + t.localizedMessage);
                }
            })
        return loginResponse;
    }
    fun startSignup(name:String , email:String ,password:String) : LiveData<ResponseBody>{
        val usersignup = signupUser(name,email, password);
        val signupresponse=MutableLiveData<ResponseBody>();


        RetrofitClient.apiService.signup(usersignup)?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Log.d("Signup Response", "${response.message()} ${response.code()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    // Process the response body here
                    if (responseBody != null) {
                        val responseString = responseBody.string()
                        val signupSuccess = Gson().fromJson(responseString, SignupSuccess::class.java)
                        // Handle the success response here
                        Log.d(TAG, "Signup Status: ${signupSuccess.message}")
                    }
                } else {
                    val errorBody = response.errorBody()
                    if (errorBody != null) {
                        val errorString = errorBody.string()
                        val signupFailure = Gson().fromJson(errorString, SignupFailure::class.java)
                        // Handle the failure response here
                        Log.d(TAG, "Signup Status: ${signupFailure.message}")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.localizedMessage}")

            }
        })




        return signupresponse;

    }
    fun firebaseSignup(name:String,email: String,password: String): LiveData<String> {
        var authStatus:String="Signup process start";
        val loginResponse = MutableLiveData<String>();
        loginResponse.value=authStatus;

        auth=FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{

            if(it.isSuccessful){
                var user: FirebaseUser?=auth.currentUser
                var userId:String =user!!.uid
                Log.d(TAG, "Success:"+name+" "+email+" "+password);

                databaseReference=FirebaseDatabase.getInstance("https://nego-a7774-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User").child(userId)


                var hasMap:HashMap<String,String> = HashMap()
                hasMap.put("userId",userId)
                hasMap.put("userName",name)
                hasMap.put("profileImage","none")

                databaseReference.setValue(hasMap).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "firebaseSignup: Signup Success!!")
                        authStatus="Signup Successful"
                        loginResponse.value=authStatus;
                    }
                    else{
                        val exception = it.exception
                        authStatus=exception?.message.toString()
                        loginResponse.value=authStatus;
                    }
                }

            }
            else{
                val exception = it.exception
                authStatus=exception?.message.toString()
                loginResponse.value=authStatus;

            }
        }

        return loginResponse;

    }
    fun firebaseLogin(usernaem:String,password:String): LiveData<Pair<FirebaseUserResult, String>> {
        var loginStatus:String="Signup process start";
        val loginResponse = MutableLiveData<Pair<FirebaseUserResult, String>>()
        loginResponse.value = Pair(FirebaseUserResult(null, null,null), loginStatus)


        auth=FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(usernaem,password).addOnCompleteListener {
            if(it.isSuccessful){

                val user = auth.currentUser
                val uid = user?.uid


                databaseReference = FirebaseDatabase.getInstance("https://nego-a7774-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User").child(uid.toString())
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Retrieve the icon and username from the dataSnapshot
                            val icon = dataSnapshot.child("profileImage").getValue(String::class.java)
                            val username = dataSnapshot.child("userName").getValue(String::class.java)
                            loginStatus="Login Success"
                            loginResponse.value = Pair(FirebaseUserResult(icon, username,uid), loginStatus)

                        } else {
                            loginStatus="Data does not exist"
                            loginResponse.value = Pair(FirebaseUserResult(null, null, uid), loginStatus)

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        loginStatus=databaseError.message.toString()
                        loginResponse.value = Pair(FirebaseUserResult(null, null,null), loginStatus)
                    }
                })


            }
            else{
                val exception = it.exception
                loginStatus=exception?.message.toString()
                loginResponse.value = Pair(FirebaseUserResult(null, null, null), loginStatus)
            }
        }

        return loginResponse
    }
    fun getUsersList(): LiveData<ArrayList<User>> {
        val userListLiveData = MutableLiveData<ArrayList<User>>()
        val userList = ArrayList<User>()

        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference = FirebaseDatabase.getInstance("https://nego-a7774-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val user = dataSnapshot.getValue(User::class.java)
                    Log.d(TAG, "onDataChange: " + user.toString())

                    if (user!!.userId == firebase.uid) {

                        Log.d(TAG, "onDataChange: $userList")
                    }
                    else{
                        Log.d(TAG, "onDataChange: $userList")
                        userList.add(user)
                    }
                }
                userListLiveData.value = userList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: " + error.message)
            }
        })

        return userListLiveData
    }

}
