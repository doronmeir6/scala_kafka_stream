# scala_kafka_stream
a docker with kafka streaming is needed.
look at the tiopic.xls file first tab upnp_protocol_raw that is an example of message sent to kafkastream docker topic,upnp_protocol_type
is how the the new data should look like after transform...jpg are shown as an example.
the rules: 
Please write a Scala code which consume from one topic, do some stuff, and write to another topic as follow:
consume massages from Kafka topic (upnp_protocol_raw)
The massages are in json format with the following attributes:
device_id (string)
timestamp (date)
data (string)
create a new attribute: device_type
for each massage, populate device_type by the following logic:
if a given string (iPhone for example) exists in the data attribute, then device_type = 'Smartphone' else 'Other'
write the results to a new topic (upnp_protocol_type)
