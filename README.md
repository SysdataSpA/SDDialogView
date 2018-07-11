# SDDialogView
An android dialog based only on view

no animation:<br />
![No Animation Example](https://github.com/SysdataSpA/SDDialogView/blob/animation_fix/no_animations.gif)<br />
default animation:<br />
![Default Animation Example](https://github.com/SysdataSpA/SDDialogView/blob/animation_fix/default_animation.gif)<br />
custom animation: <br />
![Custom Animation Example](https://github.com/SysdataSpA/SDDialogView/blob/animation_fix/custom_animation.gif)

## Usage
In the Activity where you need to show the dialog you need to do this
<br/>java:

```java
        dialogView = new SDDialogView.Builder()
                    .with(this)
                    .contentView(customDialog)
                    .cancelable(true)
                    .requestCode(REQUEST_CODE)
                    .build();
        dialogView.showDialog(mMainContainer);
```
kotlin:
```java
        dialogView = SDDialogView.Builder()
                    .with(this)
                    .contentView(customDialog)
                    .cancelable(true)
                    .requestCode(REQUEST_CODE)
                    .build();
        dialogView.showDialog(main_container);
```
By default there will be no animations on showing and closing the dialog, if you want to use animations you need to use:
```java
        builder.useAnimations(boolean)
```
There are already a fadeIn animation and a fadeOut animation for showing and closing the dialog, if you wanto to add custom animation you need to use
```java
        builder.enterAnimation(animation)
        builder.exitAnimation(animation)
```
If you want to provide add a listener when closing the dialog
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
