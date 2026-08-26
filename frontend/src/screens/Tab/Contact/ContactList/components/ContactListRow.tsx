import { Image, StyleSheet, Text, TouchableOpacity, View } from "react-native";
import { ContactSummaryDto } from "../../../../../api/contactApi.ts";

type ContactListRowProps = {
  contact: ContactSummaryDto;
};

function ContactListRow({ contact }: ContactListRowProps) {
  return (
    <View style={styles.container}>
      <TouchableOpacity style={styles.touchable}>
        <Image
          source={require('../../../../../assets/images/placeholder-avatar.png')}
          style={styles.avatar}
        />
        <Text style={styles.userName}>{contact.userName}</Text>
      </TouchableOpacity>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    width: '100%',
  },
  touchable: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    padding: 12,
  },
  avatar: {
    width: 50,
    height: 50
  },
  userName: {
    marginLeft: 12,
    fontWeight: "bold",
    fontSize: 18
  }
})

export default ContactListRow