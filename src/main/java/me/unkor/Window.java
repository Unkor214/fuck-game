package me.unkor.jade;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

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

    //пустой класс окна
    private static  Window window = null;

    private Window() {
        //1920x1080
        this.width = 1920;
        this.height = 1080;

        this.title = "Super mario";
    }

    public static Window get() {
        //если переменная окна пустая
        if (window == null) {
            window = new Window();
        }

        return Window.window;
    }

    //фун. запуска
    public void run() {
        System.out.println("Hello, lwjgl");

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
        if (glfwWindow == NULL)
            throw new IllegalStateException("Window has been not create");

        //Выставление контекста на главное окно
        glfwMakeContextCurrent(glfwWindow);
        //в-синх.
        glfwSwapInterval(1);

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
        //пока окно не закрыто
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            glClearColor(1.0f, 0.5f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);
        }
    }
}
