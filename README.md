

# Starter

中文[Readme.md](https://github.com/boybeak/Starter/blob/master/README_cn.md)

There are 4 libraries in this project:[ **Starter**](https://github.com/boybeak/Starter#starter--), [**Drag-Exit**](https://github.com/boybeak/Starter#drag-exit--), [**Permission**](https://github.com/boybeak/Starter#permission--), [**Picker**](https://github.com/boybeak/Starter#picker--), [SAFR](https://github.com/boybeak/Starter#safr--)

## Starter[ ![Starter](https://api.bintray.com/packages/boybeak/nulldreams/starter/images/download.svg) ](https://bintray.com/boybeak/nulldreams/starter/_latestVersion)

This is a quick start package, including BaseActivity, BaseFragment and utils classes;

```groovy
implementation 'com.github.boybeak:starter:x.y.z'
```

## Drag-Exit[ ![Download](https://api.bintray.com/packages/boybeak/nulldreams/drag-exit/images/download.svg) ](https://bintray.com/boybeak/nulldreams/drag-exit/_latestVersion)

```groovy
implementation 'com.github.boybeak:drag-exit:x.y.z'
```

![demo gif](https://github.com/boybeak/Starter/blob/master/gif/drag-exit.gif)

There's only one FrameLayout's sub class **DragExitLayout**. Use the layout like this:

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.github.boybeak.de.DragExitLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/drag_exit_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:lazy="0.25"
    app:stuckEffect="true"
    app:activeEdges="left">
	<WebView 
        android:id="@+id/web_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        >
    </WebView>
</com.github.boybeak.de.DragExitLayout>
```

```kotlin
class DragExitActivity : AppCompatActivity() {
    
    private val dragListener = object : DragExitLayout.OnExitListener {
        override fun onExit() {
			finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_drag_exit)
        
        drag_exit_layout.setOnExitListener(dragListener)
        
        web_view.webViewClient = WebViewClient()
        web_view.webChromeClient = WebChromeClient()
        web_view.loadUrl("https://github.com/boybeak/Starter")
    }
}
```

Some attributes as below:

- **activeEdges** - *enum*: 3 values are available, **left**, **right** and **both**, which edge can active the drag gesture.
- **exitLineRatio** - *float*: Available values in range (0, 1), the exit action trigger ratio of layout width.
- **stuckEffect** - *boolean*:  Whether should the child view stuck at exitLineRation position or not.
- **lazy** - *float*: Available values in range (0, 1). If set to 0.5, your finger move 100 pixels, the child view actually move 50 pixels.
- **alphaEffect** - *boolean*: Whether the child view change its alpha when moving.
- **alphaMin** - *float*: The minimum value of the child view can be. Once the child view moves to exitLineRation position, the alpha will be alphaMin.
- **scaleEffect** - *boolean*, same as alphaEffect
- **scaleMin** - *float*: same as alphaMin



## Permission[ ![Download](https://api.bintray.com/packages/boybeak/nulldreams/permission/images/download.svg) ](https://bintray.com/boybeak/nulldreams/permission/_latestVersion)

This is a permission helper. Quickly grant permissions.

```groovy
implementation 'com.github.boybeak:permission:x.y.z'
```

```kotlin
PH.ask(Manifest.permission.WRITE_EXTERNAL_STORAGE, 
	Manifest.permission.CAMERA, 
	Manifest.permission.RECORD_AUDIO).go(this, object : Callback {
            override fun onGranted(permissions: MutableList<String>) {
				//TODO do what you want
            }

            override fun onDenied(permission: String) {
				//TODO do what you want
            }

        })
```

**PH** is short for Permission Helper.



## Picker[ ![Picker](https://api.bintray.com/packages/boybeak/nulldreams/picker/images/download.svg) ](https://bintray.com/boybeak/nulldreams/picker/_latestVersion)

A helper library for getting images and videos quickly.

```groovy
implementation 'com.github.boybeak:picker:x.y.z'
```

```kotlin
// Get multiple images from gallery
Picker.gallery().image().multiple(true)
	.go(this@PickerActivity, object : MultipleCallback {
		override fun onGet(id: String, uris: MutableList<Uri>, files: MutableList<File>) {
		    adapter!!.addAll(files, Converter<File, FileImpl> { data, _ -> FileImpl(data) }).autoNotify()
		}

		override fun onCancel(id: String) {

		}

	    })
```

```kotlin
// Get one image from camera
val dir = File(externalCacheDir, "images")
if (!dir.exists()) {
    dir.mkdirs()
}

val cameraTempFile = File(dir, System.currentTimeMillis().toString() + ".jpg")
val uri = FileProvider.getUriForFile(this@PickerActivity, "$packageName.provider", cameraTempFile)
Picker.camera().image().output(uri, cameraTempFile)
	.go(this@PickerActivity, object : SingleCallback {
		override fun onGet(id: String, uri: Uri, file: File) {
		    adapter!!.add(FileImpl(file)).autoNotify()
		}

		override fun onCancel(id: String) {

		}

	    })
```

With this library, you can get images easily from gallery and camera. Support getting multiple images and videos from gallery, and getting one image or video from camera.



## SAFR[ ![Download](https://api.bintray.com/packages/boybeak/nulldreams/safr/images/download.svg) ](https://bintray.com/boybeak/nulldreams/safr/_latestVersion)

```groovy
implementation 'com.github.boybeak:safr:x.y.z'
```

**SAFR** is short for **startActivityForResult**.

```kotlin
SAFR.newInstance().extras {
            //fill data here
        }.byAction(Intent.ACTION_GET_CONTENT)
	.startActivityForResult(this, 100, object : Callback {
            override fun onResult(requestCode: Int, resultCode: Int, data: Intent?) {

            }

        })
```

