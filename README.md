# AutoBanner

## 利用ViewPager制作可以滑动的banner
* 继承AutoPagerAdapter 达到无限翻页效果.
* 利用PageTransformer 完成滚动的特效. 其中特效 Depth,ZoomOut 来自互联网 Margin 来源本人
* 利用反射修改mScroller或修改setCurrentItem 方法,达到修改翻页速度的效果.

## 利用ViewFlipper AdapterFlipper 制作不需要滑动的banner
* viewFlipper 利用animation 实现过场动画,而AdapterViewFlipper利用ObjectAnimator实现过场动画
有区别
* xml中做ObjectAnimator有点蛋疼,研究中,待续.

## indicator
> IndicatorLayout
* 仿照TabLayout 方式,定义内部类View画圆点
* 自定义IndicatorLayout 通过属性动画实现indicator移动.
* 或者与ViewPager关联,通过offset移动指示点.

> IndicatorView
* 重写View,用canvas画圆点
* 通过Scroller 和View.invalidate()方法,绘制动画.