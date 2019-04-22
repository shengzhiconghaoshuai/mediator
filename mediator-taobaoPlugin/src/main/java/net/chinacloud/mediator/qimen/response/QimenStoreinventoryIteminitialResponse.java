package net.chinacloud.mediator.qimen.response;



import java.util.List;

import net.chinacloud.mediator.qimen.domain.QinmenError;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;
@XStreamAlias("response")
public class QimenStoreinventoryIteminitialResponse extends QimenResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	List<QinmenError> errorDescriptions;
	
	@Override
	public XStream getXStream() {
		XStream xstream = new XStream(new DomDriver()) {
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new MapperWrapper(next) {
					@SuppressWarnings("unchecked")
					public boolean shouldSerializeMember(Class definedIn,
							String fieldName) {
						return definedIn != Object.class ? super
								.shouldSerializeMember(definedIn, fieldName)
								: false;
					}
				};
			}
		};
		xstream.processAnnotations(QimenStoreinventoryIteminitialResponse.class);
		return xstream;
	}

	public List<QinmenError> getErrorDescriptions() {
		return errorDescriptions;
	}

	public void setErrorDescriptions(List<QinmenError> errorDescriptions) {
		this.errorDescriptions = errorDescriptions;
	}


	
}
