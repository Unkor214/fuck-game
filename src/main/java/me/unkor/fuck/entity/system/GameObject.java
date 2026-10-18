package me.unkor.fuck.entity.system;

import me.unkor.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name;
    private List<Component> components;

    public Transform transform;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
    }

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
    }

    //получение компонента
    public <T extends Component> T getComponent(Class<T> componentClass) {
        //берем компоненты из списка 'components'
        for (Component component : components) {
            try {
                //если указаный компонент совподает с элементом из списка,
                //то возрашаем его
                if (componentClass.isAssignableFrom(component.getClass())) {
                    return componentClass.cast(component);
                }
            } catch (ClassCastException e) {
                //отладка, на всякий случий
                e.printStackTrace();
                assert false : "Error : Casting component";
            }

        }

        return null;
    }

    //удалление компонента
    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i=0; i < components.size(); i++) {
            //выдаем пременой 'component' текуший элемент в списке
            Component component = components.get(i);
            //если указаный компонент соответствует компоненту из 'component',
            //то мы его удаляем
            if (componentClass.isAssignableFrom(component.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    //Добавления компонентов в список
    public void addComponent(Component component) {
        this.components.add(component);
        component.gameObject = this;
    }

    //функция обновления, для оброботки тиков и т.д
    public void update(float dt) {
        for (int i=0; i < components.size(); i++) {
            // запуск функции обновления, для всех объектов в списке
            components.get(i).update(dt);
        }
    }

    //функция старта, для оброботки начальных данных и т.д
    public void start() {
        //запуск функции старт у всех объектво в списке
        for (int i=0; i < components.size(); i++) {
            components.get(i).start();
        }
    }
}