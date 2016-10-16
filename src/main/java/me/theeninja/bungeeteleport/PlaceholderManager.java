package me.theeninja.bungeeteleport;

/**
 * Created by TheeNinja on 10/15/2016.
 */

/**
 * Utility class for the BungeeTeleport plugin
 *
 * @author TheeNinja
 */
public class PlaceholderManager {

    private Placeholder[] registeredPlaceholders;

    /**
     * Constructor for case-specific placeholder managers.
     *
     * @param registeredPlaceholders - Represents the placeholders to check and manage for this
     *                               placeholder manager
     */
    public PlaceholderManager(Placeholder[] registeredPlaceholders) {

        this.registeredPlaceholders = registeredPlaceholders;
    }

    /**
     *
     * @param string - String including raw placeholders
     * @return string with updated placeholders
     */
    public String replacePlaceholders(String string) {

        for (Placeholder placeholder: registeredPlaceholders) {

            string = placeholder.getPlaceholderAction().replace(string);
        }

        return null;
    }

    /**
     * Represents individual placeholder, containing representation and
     * method of managing placeholder replacement.
     *
     * @author Theeninja
     */
    public static class Placeholder {

        private String representation;
        private PlaceholderReplaceAction replaceAction;

        /**
         * Constructor for producing attributes specific to placeholder.
         *
         * @param representation - placeholder identifier (enclosed in % in configuration file)
         * @param replaceAction - implementation containing method on how to go about the replacement of placeholder
         */
        public Placeholder(String representation, PlaceholderReplaceAction replaceAction) {

            this.representation = representation;
            this.replaceAction = replaceAction;
        }

        /**
         *
         * @return Placeholder representation
         */
        public String getRepresentation() {

            return representation;
        }

        /**
         *
         * @return Placeholder's implementation for replacing representation with another value.
         */
        public PlaceholderReplaceAction getPlaceholderAction() {

            return replaceAction;
        }
    }

    /**
     * Represents the implementation to override for specific placeholders.
     *
     * @author TheeNinja
     */
    public interface PlaceholderReplaceAction {

        /**
         * Method to replace placeholders in string with relevant case-specific values.
         *
         * @param string - string to replace placeholders in
         * @return updated string with modified placeholders
         */
        public String replace(String string);
    }
}


