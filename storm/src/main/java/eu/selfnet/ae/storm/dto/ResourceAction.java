/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto;


public class ResourceAction {
    private ResourceAction.Scale scale;
    private Object migrate;
    private Object move;
    private Object create;
    private Object destroy;
    private Object start;
    private Object stop;
    private Object restart;
    private String oid;
    private String description;

    public ResourceAction() {
    }

    public ResourceAction.Scale getScale() {
        return scale;
    }

    public void setScale(ResourceAction.Scale scale) {
        this.scale = scale;
    }

    public Object getMigrate() {
        return migrate;
    }

    public void setMigrate(Object migrate) {
        this.migrate = migrate;
    }

    public Object getMove() {
        return move;
    }

    public void setMove(Object move) {
        this.move = move;
    }

    public Object getCreate() {
        return create;
    }

    public void setCreate(Object create) {
        this.create = create;
    }

    public Object getDestroy() {
        return destroy;
    }

    public void setDestroy(Object destroy) {
        this.destroy = destroy;
    }

    public Object getStart() {
        return start;
    }

    public void setStart(Object start) {
        this.start = start;
    }

    public Object getStop() {
        return stop;
    }

    public void setStop(Object stop) {
        this.stop = stop;
    }

    public Object getRestart() {
        return restart;
    }

    public void setRestart(Object restart) {
        this.restart = restart;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    public static class Scale {

        public Scale() {
        }

        private Object in;
        private Object out;
        private Object up;
        private Object down;
        private String value;

        public Object getIn() {
            return in;
        }

        public void setIn(Object in) {
            this.in = in;
        }

        public Object getOut() {
            return out;
        }

        public void setOut(Object out) {
            this.out = out;
        }

        public Object getUp() {
            return up;
        }

        public void setUp(Object up) {
            this.up = up;
        }

        public Object getDown() {
            return down;
        }

        public void setDown(Object down) {
            this.down = down;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
