package it.polimi.ingsw.model;

public class Cloud extends Tile {

        /**
         * Default constructor
         */
        public Cloud() {
        }

        /**
         * @return
         */
        public List<Students> empty() {
            List<Students> temp = new List<Students>(students);
            students.clear();
            return students;
        }

    }
}