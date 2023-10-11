package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.myapplication.databinding.ActivityMainBinding
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink
import io.flutter.plugin.common.EventChannel.StreamHandler
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StringCodec
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setSupportActionBar(binding.toolbar)

    val navController = findNavController(R.id.nav_host_fragment_content_main)
    appBarConfiguration = AppBarConfiguration(navController.graph)
    setupActionBarWithNavController(navController, appBarConfiguration)
    binding.fab.setOnClickListener { view ->
      //跳转flutter
      toFlutter()
    }
    val flutterEngine = MyApplication.flutterEngine

    //BasicMessageChannel 使用
    val basicMessageChannel = BasicMessageChannel(
      flutterEngine.dartExecutor.binaryMessenger,
      "basicMessageChannel",
      StringCodec.INSTANCE
    )
    binding.btnTest1.setOnClickListener { view ->
      //跳转flutter
      toFlutter()
      basicMessageChannel.send("通过basicMessageChannel发送消息"){
          message: String?->
        Log.d("ssz", "接收：$message")  //message 是flutter发回给原生的消息
      }
    }

//    basicMessageChannel.setMessageHandler { message, reply ->
//      Log.d("ssz", "接收：$message")
//    }

    //MethodChannel 使用
    val methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "test2")
    binding.btnTest2.setOnClickListener {
      toFlutter()
      methodChannel.setMethodCallHandler { call, result ->
        if (call.method.equals("open")){
          Log.d("ssz", "接收flutter调用")
        }
      }
    }

    //EventChannel 使用
    val eventChannel = EventChannel(flutterEngine.dartExecutor.binaryMessenger, "test3")
    binding.btnTest3.setOnClickListener {
      toFlutter()
      eventChannel.setStreamHandler(object : StreamHandler{
        override fun onListen(arguments: Any?, events: EventSink?) {
          Log.d("ssz","执行了 onListen");
          events?.success("原生发送")
        }
        override fun onCancel(arguments: Any?) {
        }

      })

    }
  }

  private fun toFlutter() {
    startActivity(
      FlutterActivity
        .withCachedEngine("my_engine_id")
        .build(this)
    );
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    return when (item.itemId) {
      R.id.action_settings -> true
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_main)
    return navController.navigateUp(appBarConfiguration)
        || super.onSupportNavigateUp()
  }
}