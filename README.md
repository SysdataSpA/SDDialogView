# SDDialogView
An android dialog based only on view

**No animation:**

![No Animation Example](https://github.com/SysdataSpA/SDDialogView/blob/animation_fix/no_animations.gif)

**Default animation:**

![Default Animation Example](https://github.com/SysdataSpA/SDDialogView/blob/animation_fix/default_animation.gif)

**Custom animation:**

![Custom Animation Example](https://github.com/SysdataSpA/SDDialogView/blob/animation_fix/custom_animation.gif)

## Usage

**Gradle**

- **Project level `build.gradle`**
```gradle
allprojects {
    repositories {
        maven { url  'https://dl.bintray.com/sysdata/maven' }
    }
}
```
- **App level `build.gradle`**
```gradle
dependencies {
    implementation 'it.sysdata.mobile:sddialogview:1.0.0'
}
```

In the Activity where you need to show the dialog you have to do this

**Java:**

```java
        dialogView = new SDDialogView.Builder()
                    .with(this)
                    .contentView(customDialog)
                    .requestCode(REQUEST_CODE)
                    .build();
        dialogView.showDialog(mMainContainer);
```

**Kotlin:**

```java
        dialogView = SDDialogView.Builder()
                    .with(this)
                    .contentView(customDialog)
                    .requestCode(REQUEST_CODE)
                    .build();
        dialogView.showDialog(main_container);
```
By default the dialog can be closed bhy clicking on the grey part, outside of the dialog, if you want to close the dialog only by calling closeDialog add
```java
        builder.cancelable(false)
```
By default there will be no animations on showing and closing the dialog, if you want to use animations you need to use:
```java
        builder.useAnimations(true)
```
There are already a fadeIn animation and a fadeOut animation for showing and closing the dialog, if you wanto to add custom animation you need to use
```java
        builder.enterAnimation(animation)
        builder.exitAnimation(animation)
```
If you want to provide a listener when closing the dialog
```java
        builder.onDialogCloseListener(listener)
```
listener has to implement ``` onDialogCloseListener ``` interface and implement a method ``` onClose(int requestCode, int resultCode)```
The custom dialog have to implement ``` Compatible ``` interface to bind the parento to close the dailog for example

## License
Copyright (C) 2017 Sysdata S.p.A.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
