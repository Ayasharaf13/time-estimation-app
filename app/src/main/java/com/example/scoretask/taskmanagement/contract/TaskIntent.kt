package com.example.scoretask.taskmanagement.contract

sealed interface TaskIntent {

    // 1️⃣ أكشن العرض (Display): مش محتاج داتا، هو بس بيطلب تحميل كل التاسكات
    //object DisplayTasks : TaskIntent

    // 2️⃣ أكشن الحفظ (Save): بنحمل مع الكبسولة كائن التاسك اللي عايزين نحفظه
    object SaveTask : TaskIntent

    // 3️⃣ أكشن الحذف (Delete): مش محتاجين التاسك كلها، كفاية نبعت الـ ID بتاعها عشان نمسحها
    data class DeleteTask(val taskId: Long) : TaskIntent

    // 4️⃣ أكشن التعديل (Edit): بنبعت كائن التاسك بعد ما المستخدم عدل بياناتها عشان تتحدث
    // data class EditTask(val task: TaskTemplateEntity) : TaskIntent
    object EditTask : TaskIntent

    object CancelTask : TaskIntent
    object CancelTime : TaskIntent

    object DismissDialog : TaskIntent
    data class Confirm(val idTask: Long, val titleTask: String) : TaskIntent
    object Cancel : TaskIntent


    data class TitleChanged(
        val title: String
    ) : TaskIntent

    data class HoursChanged(
        val hours: String
    ) : TaskIntent

    data class MinutesChanged(
        val minutes: String
    ) : TaskIntent


}