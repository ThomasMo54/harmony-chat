import { SafeAreaView } from 'react-native-safe-area-context';
import {
  FlatList,
  Platform,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import AppIcon from '../../../../components/AppIcon/AppIcon.tsx';
import { COLORS } from '../../../../constants/colors.ts';
import { findContacts } from '../../../../api/contactApi.ts';
import ContactListRow from './components/ContactListRow.tsx';
import { useAuth } from '../../../../context/AuthContext.tsx';
import usePaginatedList from '../../../../hooks/usePaginatedList.ts';

const contactsPerPage = 20;

function ContactListScreen() {
  const { items: contacts, loadMore: loadMoreContacts } = usePaginatedList(
    page => findContacts(page, contactsPerPage),
  );
  const { logout } = useAuth();

  return (
    <SafeAreaView style={styles.safe} edges={['top', 'bottom']}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => console.log('Pressed')}>
          <AppIcon name="user-plus" size={20} color={COLORS.primary} />
        </TouchableOpacity>
        <TouchableOpacity onPress={logout}>
          <Text>Disconnect</Text>
        </TouchableOpacity>
      </View>
      <FlatList
        data={contacts}
        keyExtractor={item => item.userId}
        renderItem={({ item }) => <ContactListRow contact={item} />}
        onEndReached={() => loadMoreContacts()}
        ItemSeparatorComponent={() => (
          <View style={{ height: 1, backgroundColor: '#eee' }} />
        )}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: {
    flex: 1,
    backgroundColor: '#fafafa',
  },
  container: {
    flex: 1,
    alignItems: 'center',
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    width: '100%',
    padding: 12,
    paddingHorizontal: 16,
    backgroundColor: '#fff',
    ...Platform.select({
      ios: {
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.15,
        shadowRadius: 4,
      },
      android: {
        elevation: 4,
      },
    }),
  },
  addContactButton: {
    alignSelf: 'flex-end',
  },
});

export default ContactListScreen;
