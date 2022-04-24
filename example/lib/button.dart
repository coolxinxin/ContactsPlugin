import 'package:flutter/material.dart';

class Button extends StatelessWidget {
  final String text;
  final double textSize;
  final Color textColor;
  final double height;
  final double width;
  final EdgeInsetsGeometry margin;
  final VoidCallback click;
  final double radius;

  const Button({
    Key? key,
    required this.text,
    required this.margin,
    required this.click,
    this.textSize = 18.0,
    this.textColor = Colors.white,
    this.height = 44,
    this.width = double.infinity,
    this.radius = 25,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: width,
      height: height,
      margin: margin,
      // margin: const EdgeInsets.fromLTRB(34.0, 35.0, 34.0, 0.0),
      decoration: BoxDecoration(
          gradient: const LinearGradient(
              colors: [Color(0xffFF93F6), Color(0xffDA36D6)]),
          borderRadius: BorderRadius.circular(radius)),
      child: ElevatedButton(
        style: ElevatedButton.styleFrom(
          shape:
              RoundedRectangleBorder(borderRadius: BorderRadius.circular(radius)),
          primary: Colors.transparent,
          shadowColor: Colors.transparent, // 设为透明色
          elevation: 0, // 正常时阴影隐藏
        ),
        onPressed: click,
        child: Container(
          alignment: Alignment.center,
          height: height,
          child: Text(
            text,
            textAlign: TextAlign.center,
            style: TextStyle(color: textColor, fontSize: textSize),
          ),
        ),
      ),
    );
  }
}
