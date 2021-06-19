# veriff-sdk
* Quick summary
   - This is the Android SDK for ML Vision.
   - Version 1.0

### How do I get set up? ###

* Summary of set up\
   Import Project to Android Studio
* Configuration\
    Android Studio 4.2.1\
    compileSdkVersion = 30\
    minSdkVersion = 21\
    targetSdkVersion = 30\
    buildtoolVersion = '29.0.2'
* Dependencies\
    Kotlin\
    Databinding\
    Android X\
    ML Kit 
* How to run tests\
    Junit
* Deployment instructions

### veriff-core : ###
   Core accelerators for SDK 
   
### veriff-components: ###
   consists of UI Components which can be included to app 
   
### veriff-identity: ### 
   Consists of Utility and repository and usecases for ML Vision 
   
   
###  How to use: ###

## Option 1 : ##

Step 1: 

Build Live Camera View for Face / OCR detection.

Inlcude VeriffScannerView in the layout where you need add live scanner.

```
<com.veriff.sdk.components.view.VeriffScannerView
        android:id="@+id/veriffScannerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>
        
   ```
   
 Step 2 :
   
 Initialize the Veriff SDK in Application class do not forget to add Application class in Manifest.
 
```
class SampleApp: Application() {
    override fun onCreate() {
        super.onCreate()
        VeriffApp.initialize(this)
    }
}
```
Please add Sample App in Manifest 
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.veriff.sample">

    <application
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:name=".SampleApp"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:requestLegacyExternalStorage="true"
        android:theme="@style/Theme.Veriff">
        <activity
            android:name="com.veriff.sample.MainActivity"
            android:label="@string/app_name">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

Step 3 :

in Fragment where VeriffScannerView is been included initilize the VeriffIdentityManager and check for Permission's 

```
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVerifyIdentity()
    }

    private fun initVerifyIdentity() {

        faceRecViewModel.checkPermission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                var allPermissionGranted = false
                permissions.entries.forEach {
                    allPermissionGranted = it.value
                }
                if (allPermissionGranted) {
                    faceRecViewModel.veriffIdentityManager?.onRequestPermissionsResult()
                }
            }

        faceRecViewModel.veriffIdentityManager = activity?.let {
            faceRecViewModel.checkPermission?.let { checkPermission ->
                VeriffIdentityManager(
                    VeriffApp.getInstance(),
                    it,
                    checkPermission,
                    binding.veriffScannerView,
                    faceRecViewModel.visionType,
                    this
                )
            }
        }
    }
```

## Option 2: ##

Step 1:

 To Running Detection using Repository for an Bitmap 
 
 Usecase for Face Detection by supplying Bitmap
 
 ```
 class FaceRecViewModel @Inject constructor(private val repository: IFaceRecognitionRepository) :
    ViewModel() {
    internal var checkPermission: ActivityResultLauncher<Array<String>>? = null
    var veriffIdentityManager: VeriffIdentityManager<List<Face>>? = null
    val visionType: VisionType = VisionType.Face
    val faceRecData = MutableLiveData<List<Face>>()

    suspend fun runFaceDetection(image: Bitmap) {
        val inputImage = InputImage.fromBitmap(image, 0)
        faceRecData.postValue(
            FaceRecognitionUseCase((repository as FaceRecognitionRepository)).execute(
                FaceRecognitionUseCase.Params(inputImage)
            ).value
        )
    }
}

```
Usecase for Text Detection by supplying Bitmap

```
class TextRecViewModel @Inject constructor(private val repository: ITextRecognitionRepository) :
    ViewModel() {
    internal var checkPermission: ActivityResultLauncher<Array<String>>? = null
    var veriffIdentityManager: VeriffIdentityManager<Text>? = null
    val visionType: VisionType = VisionType.OCR
    var textRecData: MutableLiveData<Text> = MutableLiveData<Text>()

    suspend fun runTextRecognition(image: Bitmap) {
        val inputImage = InputImage.fromBitmap(image, 0)
        textRecData.postValue(
            TextRecognitionUseCase((repository as TextRecognitionRepository)).execute(
                TextRecognitionUseCase.Params(inputImage)
            ).value
        )
    }
}
``` 
 


