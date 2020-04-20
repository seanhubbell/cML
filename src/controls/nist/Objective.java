/*
Copyright 2019 Sean C. Hubbell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package controls.nist;

import java.util.ArrayList;
import java.util.List;

/**
 * The Objective class is the data provided from NIST SP 800-53 and 800-53a.
 * Revision 4.
 * 
 * @author Sean C. Hubbell
 *
 */
public class Objective {
	public String number = null;
	public String decision = null;
	public List<Objective> objectives = new ArrayList<Objective>();
}
