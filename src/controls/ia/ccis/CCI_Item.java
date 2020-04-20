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

package controls.ia.ccis;

import java.util.ArrayList;
import java.util.List;

/**
 * The IA CCI Item data class.
 * 
 * @author Sean C. Hubbell
 *
 */
public class CCI_Item {
	public String id = null;
	public String status = null;
	public String publishDate = null;
	public String contributor = null;
	public String definition = null;
	public String parameter = null;
	public String note = null;
	public String type = null;
	public List<Reference> references = new ArrayList<Reference>();
}
