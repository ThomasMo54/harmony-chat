import { Image, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { ContactSummaryDto } from '@/api/contactApi.ts';
import { ReactNode } from 'react';

type ContactListRowProps = {
  children?: ReactNode;
  contact: ContactSummaryDto;
};

function ContactListRow({ children, contact }: ContactListRowProps) {
  return (
    <View style={styles.container}>
      <TouchableOpacity style={styles.touchable}>
        <Image
          source={require('@/assets/images/placeholder-avatar.png')}
          style={styles.avatar}
        />
        <Text style={styles.userName}>{contact.userName}</Text>
        <View style={styles.contentWrapper}>{children}</View>
      </TouchableOpacity>
    </View>
  );
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
    width: 45,
    height: 45,
  },
  userName: {
    marginLeft: 12,
    fontWeight: 'bold',
    fontSize: 18,
  },
  contentWrapper: {
    flex: 1,
  },
});

export default ContactListRow;
