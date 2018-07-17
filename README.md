

# Starter

There are 4 libraries in this project.

## Starter[ ![Starter](https://api.bintray.com/packages/boybeak/nulldreams/starter/images/download.svg) ](https://bintray.com/boybeak/nulldreams/starter/_latestVersion)

This is a quick start package, including BaseActivity, BaseFragment and utils classes;

```groovy
implementation 'com.github.boybeak:starter:0.0.6'
```



## Permission

This is a permission helper. Quickly grant permissions.

```groovy
implementation 'com.github.boybeak:permission:1.0.0'
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
implementation 'com.github.boybeak:picker:1.0.2'
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
implementation 'com.github.boybeak:safr:0.0.4'
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

