package com.comm.net_work.down_load

import android.content.Context

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.net_work.down_load
 * ClassName: DownloadWorker
 * Author:ShiMingShi
 * CreateDate: 2022/11/2/002 14:25
 * Description:下载器-文件下载
 */
class DownloadWorker(context: Context) {

    companion object {
        const val KEY_INPUT_URL = "KEY_INPUT_URL"
        const val KEY_OUT_PUT_URL = "KEY_OUT_URL"
        const val KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME"
        /*** WorkManager.getInstance(applicationContext)
         *       .getWorkInfoByIdLiveData(checkDisk.id)
         *       .observe(this, object : Observer<WorkInfo> {
         *            override fun onChanged(t: WorkInfo?) {
         *              // 任务执行完毕之后，会在这里获取到返回的结果
         *              if(t?.state == WorkInfo.State.RUNNING) {
         *                  Log.d("TEST", "Work progress --- ${t.progress.getInt("progress", 0)}")
         *              } else if(t?.state == WorkInfo.State.SUCCEEDED){
         *                  Toast.makeText(this@MainActivity, "Check disk success", Toast.LENGTH_LONG).show()
         *              } else if(t?.state == WorkInfo.State.FAILED){
         *                  Toast.makeText(this@MainActivity, "Check disk failed", Toast.LENGTH_LONG).show()
         *              }
         *          }
         *})
         */
//        fun startDownload(context: Context, downloadUrl: String, outputFile: String, fileName: String): UUID {
//            val inputData: Data = Data.Builder().apply {
//                putString(KEY_INPUT_URL, downloadUrl)
//                putString(KEY_OUTPUT_FILE_NAME, fileName)
//                putString(KEY_OUT_PUT_URL, outputFile)
//            }.build()
//            val request = OneTimeWorkRequestBuilder<DownloadWorker>().setInputData(inputData).build()
//            WorkManager.getInstance(context).enqueue(request)
//            return request.id
        }

}