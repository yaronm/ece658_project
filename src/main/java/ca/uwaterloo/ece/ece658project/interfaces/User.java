package ca.uwaterloo.ece.ece658project.interfaces;

import java.util.Collection;

public class User {

		String name, email;
		Collection<Long> potlucks;

		public User(String name, String email, Collection<Long> potlucks) {
			this.name = name;
			this.email = email;
			this.potlucks = potlucks;
		}

		public String getName() {
			return name;
		}

		public String getEmail() {
			return email;
		}

		public Collection<Long> getPotlucks() {
			return potlucks;
		}
		
		@Override
		public int hashCode() {
			return email.hashCode();
		}
		
		@Override
		public boolean equals(Object other) {
			if (other instanceof User) {
				User otherUser = (User) other;
				return getEmail() == otherUser.getEmail();
			}
			return false;
		}
		
}
