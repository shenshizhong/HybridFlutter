package com.example.myapplication;

import android.app.Application;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class MyApplication extends Application {
  public static FlutterEngine flutterEngine;
  public static FlutterEngine flutterEngine2;

  @Override
  public void onCreate() {
    super.onCreate();
    flutterEngine2 = new FlutterEngine(this);

    // 创建一个Flutter引擎
    flutterEngine = new FlutterEngine(this);
    //初始化需要跳转的页面
    flutterEngine.getNavigationChannel().setInitialRoute("/test3");
    // 开始执行 Dart 代码来预热flutter引擎
    flutterEngine.getDartExecutor().executeDartEntrypoint(
      DartExecutor.DartEntrypoint.createDefault()
    );

    //缓存Flutter引擎用来开启FlutterActivity
    FlutterEngineCache
      .getInstance()
      .put("my_engine_id", flutterEngine);
  }
}
