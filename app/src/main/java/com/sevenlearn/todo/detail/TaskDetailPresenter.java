package com.sevenlearn.todo.detail;

import com.sevenlearn.todo.main.MainActivity;
import com.sevenlearn.todo.model.Task;
import com.sevenlearn.todo.model.TaskDao;

public class TaskDetailPresenter implements TaskDetailContract.Presenter {
    private TaskDao taskDao;
    private TaskDetailContract.View view;
    private Task task;

    public TaskDetailPresenter(TaskDao taskDao, Task task) {
        this.taskDao = taskDao;
        this.task = task;
    }

    @Override
    public void deleteTask() {
        if (task != null) {
            int result = taskDao.delete(task);
            if (result > 0) {
                view.returnResult(MainActivity.RESULT_CODE_DELETE_TASK, task);
            }
        }
    }

    @Override
    public void saveChanges(int importance, String title) {
        if (title.isEmpty()) {
            view.showError("Enter task title");
            return;
        }

        if (task == null) {
            Task task = new Task();
            task.setTitle(title);
            task.setImportance(importance);
            long id = taskDao.add(task);
            task.setId(id);

            view.returnResult(MainActivity.RESULT_CODE_ADD_TASK, task);
        } else {
            task.setTitle(title);
            task.setImportance(importance);
            int result = taskDao.update(task);
            if (result > 0) {
                view.returnResult(MainActivity.RESULT_CODE_UPDATE_TASK, task);
            }
        }


    }

    @Override
    public void onAttach(TaskDetailContract.View view) {
        this.view = view;
        if (task != null) {
            view.setDeleteButtonVisibility(true);
            view.showTask(task);
        }
    }

    @Override
    public void onDetach() {

    }
}
