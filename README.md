# Drag---TubeAnimationV2

## How to make animation like youtube new? This is the demo application for you to answer the above question


This application is built based on my understanding of android, so it may not be really good, I look forward to your contributions and help to make the application more complete and optimal. Thank you!!!


#### add frame top
```java
dragFrame.setTopFragment(getSupportFragmentManager(), PlayFragment.newInstance());
```

#### Add frame bottom
```java
dragFrame.setBottomFragment(getSupportFragmentManager(), DetailFragment.newInstance());
```

#### Listener
```java
dragFrame.setOnDragListener(this);
```

#### Open max
```java
dragFrame.maximize();
```

#### Open min
```java
dragFrame.minimize();
```

#### close
```java
dragFrame.close();
```

#### set height waiting frame top - frame top change the size when max
```java
dragFrame.setHeightWaiting(int heightWaiting);
```

#### set height frame top - Immediately change the size
```java
dragFrame.setHeight(int height);
```

#### check max
```java
dragFrame.isMaximized();
```

#### check min
```java
dragFrame.isMinimized();
```

#### check close
```java
dragFrame.isClosed();
```




## Libraries used on the sample project

* [Butterknife](https://github.com/JakeWharton/butterknife) 
* [CircleImageView](https://github.com/hdodenhof/CircleImageView)
