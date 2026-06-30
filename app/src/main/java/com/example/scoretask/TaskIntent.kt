package com.example.scoretask

import com.example.scoretask.model.TaskTemplateEntity


sealed interface TaskIntent {

    // 1️⃣ أكشن العرض (Display): مش محتاج داتا، هو بس بيطلب تحميل كل التاسكات
    //object DisplayTasks : TaskIntent

    // 2️⃣ أكشن الحفظ (Save): بنحمل مع الكبسولة كائن التاسك اللي عايزين نحفظه
  object SaveTask/*(/*val task: TaskTemplateEntity*/) */: TaskIntent

    // 3️⃣ أكشن الحذف (Delete): مش محتاجين التاسك كلها، كفاية نبعت الـ ID بتاعها عشان نمسحها
    data class DeleteTask(val taskId: Long) : TaskIntent

    // 4️⃣ أكشن التعديل (Edit): بنبعت كائن التاسك بعد ما المستخدم عدل بياناتها عشان تتحدث
    data class EditTask(val task: TaskTemplateEntity) : TaskIntent



    data class TitleChanged(
        val title: String
    ) : TaskIntent

    data class HoursChanged(
        val hours: String
    ) : TaskIntent

    data class MinutesChanged(
        val minutes: String
    ) : TaskIntent

  //  data object TitleConfirmed :TaskIntent

   // data object DurationConfirmed : TaskIntent
}