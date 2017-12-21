# MusicAlbum

一款非常简单的圣诞主题音乐相簿应用，适合作为节日小惊喜或者拿来练手~

效果展示（下雪的时候是会播放圣诞歌的，这里没办法展示...）

![](https://user-gold-cdn.xitu.io/2017/12/20/16073414c4e1d6fa?w=197&h=350&f=gif&s=1269132)

项目里面用到了几个之前讲过的开源库，有兴趣研究如何实现的可以去看看
* **封装了照片选取功能的工厂类**，博客链接：[Android项目实践——三行代码解决照片选择与压缩](https://juejin.im/post/5a3816e3f265da432d283560)
* **雪花飘落效果**，博客链接：[Android自定义View——从零开始实现雪花飘落效果](https://juejin.im/post/5a32b34c51882506146ef1a0)
* **可播放暂停的旋转按钮**，博客链接：[Android自定义View——从零开始实现可暂停的旋转动画效果](https://juejin.im/post/5a32b5c2518825717b1ffcc4)

除此之外还用了一个**带圣诞帽子的TextView**

![](https://user-gold-cdn.xitu.io/2017/12/20/1607350d2fd7ed11?w=300&h=73&f=png&s=5220)

这个控件实现起来很简单，自定义一个控件**继承TextView**，**在onDraw方法的super.onDraw(canvas)之后绘制一顶圣诞帽**（在super.onDraw之前绘制帽子会被字挡住）
```java
@Override
protected void onDraw(Canvas canvas) {
	super.onDraw(canvas);
	Bitmap bitmap = changeBitmapSize(drawableToBitmap(getResources().getDrawable(R.drawable.hat64)),64,64);
	canvas.save();
	canvas.rotate(-45,32,32);
	canvas.drawBitmap(bitmap,-5,-5,null);
	canvas.restore();
}
```
