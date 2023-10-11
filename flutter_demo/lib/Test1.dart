import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
class Test1 extends StatelessWidget {
  const Test1({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Test1',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  String _basicMessage = '';
  var _basicMessageChannel;
  void _incrementCounter() {
    setState(() {
      _counter++;
    });
    // _basicMessageChannel.send("我是flutter的消息");
  }
  
  @override
  void initState() {
    super.initState();

    _basicMessageChannel = const BasicMessageChannel('basicMessageChannel', StringCodec());
    _basicMessageChannel.setMessageHandler((String? message) => Future<String>((){
      setState(() {//刷新控件
        _basicMessage = "flutter接收了消息：${message!}";
      });
      return "flutter 收到了";//这里是返回信息给原生
    }));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Text(
              '当前页面是Test1',
            ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headlineMedium,
            ),
            Text("显示$_basicMessage")
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
