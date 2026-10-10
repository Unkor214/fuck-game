package me.unkor;

import me.unkor.listeners.KeyListener;
import me.unkor.listeners.MouseListener;
import me.unkor.scene.LevelEditorScene;
import me.unkor.scene.LevelScene;
import me.unkor.scene.Scene;
import me.unkor.util.Time;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.HashMap;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    //ширина, высота и название
    private int width, height;
    private String title;
    //окно
    private long glfwWindow;

    public float r, g, b, a;

    //пустой класс окна
    private static  Window window = null;

    private static Scene currentScene;
    //private static HashMap<String, Scene> sceneHashMap = new HashMap<>();

    private Window() {
        //(default) 1920x1080
        this.width = 1920;
        this.height = 1080;

        //(default)
        this.title = "Fuck-game";

        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0 :
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1 :
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false : "Invalid scene '" + newScene + "'";
                break;
        }
    }

    //Пока что не используется
    /*
    public static void addScene(Scene scene, String name) {
        sceneHashMap.put(name, scene);
    }*/

    public static Window get() {
        //если переменная окна пустая
        if (window == null) {
            window = new Window();
        }

        return Window.window;
    }

    //фун. запуска
    public void run() {
        init();
        loop();

        // Освобождение ОЗУ
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Уничтожение GLFW и отладчика
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    //инит
    public void init() {
        //Отлатчик, указаный класс ввывода err (ошибка)
        GLFWErrorCallback.createPrint(System.err).set();

        //Ввывод ошибки при не удачном ините glfw
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to init GLFW");
        }

        //Конфигурация GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Окно
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title,
                NULL, NULL);
        //Ввывод ошибки при не удачном создание окна
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Window has been not create");
        }

        //Обратные вызовы мыши
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback); //позиция x -> mousePosCallback
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback); //кнопки x -> mouseButtonCallback
        glfwSetScrollCallback(glfwWindow, MouseListener::scrollCallback); //прокрутка x -> scrollCallback
        //Обратные вызовы клавиатуры
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //Выставление контекста на главное окно
        glfwMakeContextCurrent(glfwWindow);
        //в-синх.
        glfwSwapInterval(1);

        Window.changeScene(0);

        //показать окно
        glfwShowWindow(glfwWindow);

        /*
        * это кричитески ваная строчка,
        * без нее нельзя настроить ввод даных с устроества,
        * а также она кричитески важна для LWJGL
        * так-как она взаимодействует с контекстом OGL
        * и другими контекстами
        */
        GL.createCapabilities();
    }

    //игровой цикл
    public void loop() {
        //начало и конец времени кадров
        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float deltaTime = -1.0f;

        //пока окно не закрыто
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (deltaTime >= 0)
                currentScene.update(deltaTime);

            glfwSwapBuffers(glfwWindow);

            //дельта времени (звучит пафосно)
            endTime = Time.getTime();
            deltaTime = endTime - beginTime;
            beginTime = Time.getTime();
        }
    }

    public void setResolve(int w, int h) {
        Window.get().width = w;
        Window.get().height = h;
    }

    public void setTitle(String title) {
        Window.get().title = title;
    }
}