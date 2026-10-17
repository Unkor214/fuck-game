package me.unkor.fuck.entity.components;

import me.unkor.fuck.entity.system.Component;

public class FontRender extends Component {
    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRender.class) != null) {
            System.out.println("found render");
        }
    }

    @Override
    public void update(float dt) {

    }
}
