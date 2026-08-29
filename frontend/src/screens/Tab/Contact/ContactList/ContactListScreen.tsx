import {
  SafeAreaView,
  useSafeAreaInsets,
} from 'react-native-safe-area-context';
import {
  FlatList,
  Platform,
  StatusBar,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import AppIcon from '@/components/AppIcon/AppIcon.tsx';
import { COLORS } from '@/constants/colors.ts';
import { findContacts } from '@/api/contactApi.ts';
import ContactListRow from '@/screens/Tab/Contact/components/ContactListRow.tsx';
import { useAuth } from '@/context/AuthContext.tsx';
import usePaginatedList from '@/hooks/usePaginatedList.ts';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { ContactStackParamList } from '@/navigation/types.ts';
import { useNavigation } from '@react-navigation/native';

type ContactListScreenNavigationProp = NativeStackNavigationProp<
  ContactStackParamList,
  'ContactList'
>;

const contactsPerPage = 20;

function ContactListScreen() {
  const { items: contacts, loadMore: loadMoreContacts } = usePaginatedList(
    page => findContacts(page, contactsPerPage),
  );
  const { logout } = useAuth();
  const insets = useSafeAreaInsets();
  const navigation = useNavigation<ContactListScreenNavigationProp>();

  return (
    <SafeAreaView style={styles.safe} edges={['bottom']}>
      <StatusBar barStyle="dark-content" />
      <View style={[styles.header, { paddingTop: insets.top + 12 }]}>
        <TouchableOpacity
          onPress={() => navigation.navigate('ContactRequestList')}
        >
          <AppIcon name="user-plus" size={20} color={COLORS.primary} />
        </TouchableOpacity>
        <TouchableOpacity onPress={logout}>
          <Text style={styles.disconnectText}>Disconnect</Text>
        </TouchableOpacity>
      </View>
      <FlatList
        data={contacts}
        keyExtractor={item => item.userId}
        renderItem={({ item }) => <ContactListRow contact={item} />}
        onEndReached={() => loadMoreContacts()}
        ItemSeparatorComponent={() => <View style={styles.separator} />}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: {
    flex: 1,
    backgroundColor: COLORS.bg,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    alignItems: 'center',
    gap: 16,
    width: '100%',
    paddingBottom: 12,
    paddingHorizontal: 16,
    backgroundColor: COLORS.bg,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
    ...Platform.select({
      ios: {
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.08,
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
  disconnectText: {
    color: COLORS.error,
    fontWeight: '600',
  },
  separator: {
    height: 1,
    backgroundColor: COLORS.border,
  },
});

export default ContactListScreen;
