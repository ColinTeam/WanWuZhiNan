package com.ssm.comm.media

import android.app.Activity
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hjq.permissions.Permission
import com.kongzue.dialogx.dialogs.CustomDialog
import com.kongzue.dialogx.interfaces.OnBindView
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnPermissionsInterceptListener
import com.luck.picture.lib.interfaces.OnRequestPermissionListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.ssm.comm.ext.toastError
import com.tbruyelle.rxpermissions2.RxPermissions


/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.media
 * ClassName: MediaManager
 * Author:ShiMing Shi
 * CreateDate: 2022/9/9 15:04
 * Email:shiming024@163.com
 * Description:
 */
object MediaManager {

    fun openGallery(context: AppCompatActivity?, maxSelectNum: Int,engine: ImageEngine) {
        if (context == null || context.isFinishing) {
            return
        }
        PictureSelector.create(context)
            .openGallery(SelectMimeType.TYPE_IMAGE)
            .setMaxSelectNum(maxSelectNum)
            .setImageEngine(engine)// 外部传入图片加载引擎，必传项
            .setMinSelectNum(1)
            .setImageSpanCount(4)
            .isPreviewImage(false)
            .isSelectZoomAnim(false) // 图片列表点击 缩放效果 默认true
            .setSelectionMode(SelectModeConfig.SINGLE)
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    fun openCamera(context: AppCompatActivity?,listener: OnResultCallbackListener<LocalMedia>){
        if(context == null || context.isFinishing){
            return
        }
        PictureSelector.create(context).openCamera(SelectMimeType.TYPE_IMAGE).forResult(listener)
    }


    fun openAlbum(context: AppCompatActivity?,listener: OnResultCallbackListener<LocalMedia>){
        if(context == null || context.isFinishing){
            return
        }
        PictureSelector.create(context).openSystemGallery(SelectMimeType.TYPE_IMAGE).forSystemResult(listener)
    }
    /**
     * 相册选取
     *
     * @param mContext
     * @param mSelectList
     */
    fun selectMultiplePhoto(mContext: Activity?, maxNum: Int, selectedList : List<LocalMedia> = ArrayList(), engine: ImageEngine, listener: OnResultCallbackListener<LocalMedia>) {
        if (mContext == null || mContext.isFinishing) {
            return
        }
        PictureSelector.create(mContext)
            .openGallery(SelectMimeType.TYPE_ALL) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .setMaxSelectNum(maxNum)// 最大图片选择数量
            .setMinSelectNum(1) // 最小选择数量
            .setImageEngine(engine)// 外部传入图片加载引擎，必传项
            .setImageSpanCount(4) // 每行显示个数
            .setSelectionMode(SelectModeConfig.MULTIPLE) // 多选 or 单选
            .isPreviewImage(true)
            .isPreviewAudio(true) // 是否可播放音频
            .isDisplayCamera(true) // 是否显示拍照按钮
            .isSelectZoomAnim(true) // 图片列表点击 缩放效果 默认true
            .isSyncCover(true) //同步true或异步false 压缩 默认同步
            .setSelectedData(selectedList)
            .isGif(false) // 是否显示gif图片
            .forResult(listener)
    }


    /**
     * 相册选取
     *
     * @param mContext
     * @param mSelectList
     */
    fun selectSinglePhoto(mContext: Activity?, engine: ImageEngine, listener: OnResultCallbackListener<LocalMedia>) {
        if (mContext == null || mContext.isFinishing) {
            return
        }
        PictureSelector.create(mContext)
            .openGallery(SelectMimeType.ofImage()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .setMaxSelectNum(1)// 最大图片选择数量
            .setMinSelectNum(1) // 最小选择数量
            .setImageEngine(engine)// 外部传入图片加载引擎，必传项
            .setImageSpanCount(4) // 每行显示个数
            .setSelectionMode(SelectModeConfig.SINGLE) // 多选 or 单选
            .isPreviewImage(true)
            .isPreviewAudio(false) // 是否可播放音频
            .isDisplayCamera(true) // 是否显示拍照按钮
            .isSyncCover(true) //同步true或异步false 压缩 默认同步
            .isGif(false) // 是否显示gif图片
            .setPermissionsInterceptListener(object : OnPermissionsInterceptListener{
                override fun requestPermission(
                    fragment: Fragment?,
                    permissionArray: Array<String>?,
                    call: OnRequestPermissionListener?
                ) {
                    Log.e("TAG", "requestPermission: "+permissionArray )
                    if (permissionArray == null || fragment == null || fragment.context == null) return
                    call?.let { requestPermissions(fragment, permissionArray, it) }
                }

                override fun hasPermissions(
                    fragment: Fragment,
                    permissionArray: Array<String>?
                ): Boolean {
                    return false
                }

            })
            .forResult(listener)
    }

    fun requestPermissions(
        context: Fragment,
        strings: Array<String>,
        call: OnRequestPermissionListener
    ) {
        Log.e("TAG", "requestPermissions: " )
        cycleReuqest(context, strings, 0, call)
    }
    var topTipsDialog: CustomDialog? = null
    fun cycleReuqest(
        context: Fragment,
        strings: Array<String>,
        index: Int,
        call: OnRequestPermissionListener
    ) {
        topTipsDialog?.dismiss()
        if (index == strings.size) {
            call.onCall(strings, true)
        } else {
            showDialogTop(strings[index])
            Log.e("TAG", "cycleReuqest: "+strings[index] )
            RxPermissions.getInstance(context.requireContext()).request(strings[index])
                .subscribe { granted ->
                    if (granted) {
                        cycleReuqest(context, strings, index + 1, call)
                    } else {
                        showDialog(
                            strings[index],
                            context
                        )
                    }
                }

//            XXPermissions.with(context.requireContext()) //权限弹窗
//                .permission(strings[index])
//                .request(object : OnPermissionCallback {
//                    override fun onGranted(permissions: List<String?>, allGranted: Boolean) {
//                        if (allGranted) {
//                            cycleReuqest(context, strings, index + 1, call)
//                        } else {
//                            showDialog(
//                                strings[index],
//                                context
//                            )
//                        }
//                    }
//
//                    override fun onDenied(permissions: List<String?>, doNotAskAgain: Boolean) {
//                        Log.e("TAG", "onDenied: 123123")
//                        showDialog(
//                            strings[index],
//                            context
//                        )
//                    }
//                })
        }
    }

    fun showDialog(permission: String, context: Fragment) {
        topTipsDialog?.dismiss()
        var tips =
            "需要开启相册权限，以正常使用替换头像等功能，是否去设置-应用-万物指南开启相机权限开启"
        if (permission == Permission.CAMERA) {
            tips =
                "在设置-应用-万物指南开启相机权限，用于扫一扫、选取相册照片替换头像等功能"
        }
        if (permission == Permission.READ_MEDIA_IMAGES) {
            tips =
                "需要开启相册权限，以正常使用替换头像等功能，是否去设置-应用-万物指南开启相机权限开启"
        }
        if (permission == Permission.READ_MEDIA_VIDEO) {
            tips =
                "需要开启相册权限，以正常使用替换头像功能，是否去设置-应用-万物指南开启相机权限开启"
        }
        if (permission == Permission.READ_EXTERNAL_STORAGE) {
            tips =
                "需要开启相册/存储权限，以正常使用替换头像功能，是否去设置-应用-万物指南开启相机权限开启"
        }

        toastError(tips)
    }

    fun showDialogTop(permission: String) {
        var tips =
            "万物指南获取相册权限\n在下方选择允许后，您可以使用选择相册，选择图片替换头像等功能"
        if (permission == Permission.CAMERA) {
            tips =
                "万物指南获取相机权限\n在下方选择允许后，您可以使用访问摄像头进行拍摄照片和视频"
        }
        if (permission == Permission.READ_MEDIA_IMAGES) {
            tips =
                "万物指南获取相册权限\n在下方选择允许后，您可以使用选择相册，选择图片替换头像等功能"
        }
        if (permission == Permission.READ_MEDIA_VIDEO) {
            tips =
                "万物指南获取相册权限\n在下方选择允许后，您可以使用选择相册，选择图片替换头像等功能"
        }
        if (permission == Permission.READ_EXTERNAL_STORAGE) {
            tips =
                "万物指南获取相册权限\n在下方选择允许后，您可以使用选择相册，选择图片替换头像等功能"
        }

        topTipsDialog = CustomDialog.build()
                .setCustomView(object : OnBindView<CustomDialog>(com.ssm.comm.R.layout.dialog_top) {
                    override fun onBind(dialog: CustomDialog, v: View) {
                        var textView = v.findViewById<TextView>(com.ssm.comm.R.id.txt_dialog_tip)
                        textView.text = tips
                    }
                })
                .show()
    }

    /**
     * 相册选取
     *
     * @param mContext
     * @param mSelectList
     */
    fun selectReportPhoto(mContext: Activity?, maxNum: Int, listener: OnResultCallbackListener<LocalMedia>, engine: ImageEngine) {
        if (mContext == null || mContext.isFinishing) {
            return
        }
        PictureSelector.create(mContext)
            .openGallery(SelectMimeType.TYPE_IMAGE) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .setMaxSelectNum(maxNum)// 最大图片选择数量
            .setMinSelectNum(1) // 最小选择数量
            .setImageEngine(engine)// 外部传入图片加载引擎，必传项
            .setImageSpanCount(4) // 每行显示个数
            .setSelectionMode(SelectModeConfig.MULTIPLE) // 多选 or 单选
            .isPreviewImage(true)
            .isPreviewAudio(true) // 是否可播放音频
            .isDisplayCamera(true) // 是否显示拍照按钮
            .isSelectZoomAnim(true) // 图片列表点击 缩放效果 默认true
            .isSyncCover(true) //同步true或异步false 压缩 默认同步
            .isGif(false) // 是否显示gif图片
            .forResult(listener)
    }


    //单选图片的路径//单选图片的路径
    fun getSinglePhotoUri(localMedia: LocalMedia): String? {
        var photo = if (localMedia.isCompressed) {
            localMedia.compressPath
        } else {
            val version = Build.VERSION.SDK_INT
            if (version >= 29) {
                if (TextUtils.isEmpty(localMedia.sandboxPath)) {
                    localMedia.realPath
                } else {
                    localMedia.sandboxPath
                }
            } else {
                localMedia.path
            }
        }

        return photo
    }
}