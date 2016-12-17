# AutoBanner

## 利用ViewPager制作可以滑动的banner
* 继承AutoPagerAdapter 达到无限翻页效果.
* 利用PageTransformer 完成滚动的特效. 其中特效 Depth,ZoomOut 来自互联网 Margin 来源本人
* 利用反射修改mScroller或修改setCurrentItem 方法,达到修改翻页速度的效果.

## 利用ViewFlipper AdapterFlipper 制作不需要滑动的banner

* 待续

## indicator
* 自定义IndicatorLayout 通过属性动画实现indicator移动.
* 或者与ViewPager关联,通过offset移动指示点.