package com.sevenlearn.todo.main;

import com.sevenlearn.todo.BasePresenter;
import com.sevenlearn.todo.BaseView;
import com.sevenlearn.todo.model.Task;

import java.util.List;

public interface MainContract {

    interface View extends BaseView {
        void showTasks(List<Task> tasks);

        void clearTasks();

        void updateTask(Task task);

        void addTask(Task task);

        void deleteTask(Task task);

        void setEmptyStateVisibility(boolean visible);

    }

    interface Presenter extends BasePresenter<View> {
        void onDeleteAllBtnClick();

       void onSearch(String q);

        void onTaskItemClick(Task task);

    }
}
