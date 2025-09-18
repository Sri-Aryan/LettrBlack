package com.example.letteblack.repositories

import com.example.letteblack.db.TaskDao
import com.example.letteblack.db.TaskEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.*

class TaskRepositoryImpl(
    private val dao: TaskDao,
    private val firestore: FirebaseFirestore
) : TaskRepository {

    private val collection get() = firestore.collection("tasks")

    override fun observeTasks(groupId: String) = dao.observeTasks(groupId)

    override suspend fun assignTask(
        groupId: String,
        assignerId: String,
        assigneeId: String,
        assigneeName: String,
        title: String,
        description: String,
        pointsRewarded: Int,
        dueDate: Long?
    ) {
        val now = System.currentTimeMillis()
        val task = TaskEntity(
            taskId = UUID.randomUUID().toString(),
            groupId = groupId,
            assignerId = assignerId,
            assigneeId = assigneeId,
            assigneeName = assigneeName,
            title = title,
            description = description,
            pointsRewarded = pointsRewarded,
            dueDate = dueDate,
            createdAt = now,
            updatedAt = now,
            status = "incomplete"
        )

        dao.insert(task)

//        val map = mapOf(
//            "taskId" to task.taskId,
//            "groupId" to task.groupId,
//            "assignerId" to task.assignerId,
//            "assigneeId" to task.assigneeId,
//            "title" to task.title,
//            "description" to task.description,
//            "pointsRewarded" to task.pointsRewarded,
//            "dueDate" to task.dueDate,
//            "status" to task.status,
//            "createdAt" to task.createdAt,
//            "updatedAt" to task.updatedAt,
//            "status" to task.status
//        )
//        collection.document(task.taskId).set(map).await()
    }

    override suspend fun updateStatus(taskId: String, status: String) {
        val now = System.currentTimeMillis()
        dao.updateStatus(taskId, status, now)
//        collection.document(taskId).update(
//            mapOf("status" to status, "updatedAt" to now)
//        ).await()
    }

    override suspend fun updateTask(
        taskId: String,
        title: String,
        description: String,
        dueDate: Long?,
        pointsRewarded: Int,
        assigneeId: String
    ) {
        val now = System.currentTimeMillis()
        dao.updateTask(taskId, title, description, dueDate, pointsRewarded,assigneeId, now)

//        collection.document(taskId).update(
//            mapOf(
//                "title" to title,
//                "description" to description,
//                "dueDate" to dueDate,
//                "pointsRewarded" to pointsRewarded,
//                "updatedAt" to now
//            )
//        ).await()
    }

    override suspend fun deleteTask(taskId: String) {
        dao.delete(taskId)
        collection.document(taskId).delete().await()
    }

    override fun getTaskById(taskId: String): Flow<TaskEntity?> {
        return dao.getTaskById(taskId)
    }

    override suspend fun getTaskByIdOnce(taskId: String): TaskEntity? {
        return dao.getTaskByIdOnce(taskId)
    }

}