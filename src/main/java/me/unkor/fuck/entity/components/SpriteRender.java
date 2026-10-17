package me.unkor.fuck.entity.components;

import me.unkor.fuck.entity.system.Component;

public class SpriteRender extends Component {
    private boolean firstTime = false;

    @Override
    public void start() {
        System.out.println("sr start");
    }

    @Override
    public void update(float dt) {
        if (!firstTime) {
            System.out.println("sr update");
            firstTime = true;
        }
    }
}
