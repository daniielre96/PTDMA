package com.example.myapplication.Global;

import android.app.Application;

public class GlobalVars extends Application {

    private boolean MainToDoWelcome = false;
    private boolean CreateModiftyTaskWelcome = false;
    private boolean MainEventsWelcome = false;
    private boolean CreateModifyEventWelcome = false;
    private boolean MainShoppingListWelcome = false;
    private boolean CreateModifyShoppingList = false;

    public boolean isMainToDoWelcome() {
        return MainToDoWelcome;
    }

    public void setMainToDoWelcome(boolean mainToDoWelcome) {
        MainToDoWelcome = mainToDoWelcome;
    }

    public boolean isCreateModiftyTaskWelcome() {
        return CreateModiftyTaskWelcome;
    }

    public void setCreateModiftyTaskWelcome(boolean createModiftyTaskWelcome) {
        CreateModiftyTaskWelcome = createModiftyTaskWelcome;
    }

    public boolean isMainEventsWelcome() {
        return MainEventsWelcome;
    }

    public void setMainEventsWelcome(boolean mainEventsWelcome) {
        MainEventsWelcome = mainEventsWelcome;
    }

    public boolean isCreateModifyEventWelcome() {
        return CreateModifyEventWelcome;
    }

    public void setCreateModifyEventWelcome(boolean createModifyEventWelcome) {
        CreateModifyEventWelcome = createModifyEventWelcome;
    }

    public boolean isMainShoppingListWelcome() {
        return MainShoppingListWelcome;
    }

    public void setMainShoppingListWelcome(boolean mainShoppingListWelcome) {
        MainShoppingListWelcome = mainShoppingListWelcome;
    }

    public boolean isCreateModifyShoppingList() {
        return CreateModifyShoppingList;
    }

    public void setCreateModifyShoppingList(boolean createModifyShoppingList) {
        CreateModifyShoppingList = createModifyShoppingList;
    }
}
