

# Starter

本项目中共有4个库.

## Starter[ ![Starter](https://api.bintray.com/packages/boybeak/nulldreams/starter/images/download.svg) ](https://bintray.com/boybeak/nulldreams/starter/_latestVersion)

这是一个用于项目快速启动的库, 包含 BaseActivity, BaseFragment 以及一些工具类。

```groovy
implementation 'com.github.boybeak:starter:x.y.z'
```



## Permission

这是一个动态权限的辅助库。能够快速的获取权限，而不用层层检查每次都重写权限结果回调方法。

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

**PH** 是 Permission Helper的简写.



## Picker[ ![Picker](https://api.bintray.com/packages/boybeak/nulldreams/picker/images/download.svg) ](https://bintray.com/boybeak/nulldreams/picker/_latestVersion)

获取图片视频的辅助类。

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

用这个库，你能够从系统相册中单选或者多选图片或者视频，可以从相机中获取单个图片或者视频，同时视频支持设置最大时长和视频质量。



## SAFR[ ![Download](https://api.bintray.com/packages/boybeak/nulldreams/safr/images/download.svg) ](https://bintray.com/boybeak/nulldreams/safr/_latestVersion)

```groovy
implementation 'com.github.boybeak:safr:x.y.z'
```

**SAFR** 是 **startActivityForResult** 的简写。只为在一个回调中处理结果，而不用重写onActivityResult。

```kotlin
SAFR.newInstance().extras {
            //fill data here
        }.byAction(Intent.ACTION_GET_CONTENT)
	.startActivityForResult(this, 100, object : Callback {
            override fun onResult(requestCode: Int, resultCode: Int, data: Intent?) {

            }

        })
```

