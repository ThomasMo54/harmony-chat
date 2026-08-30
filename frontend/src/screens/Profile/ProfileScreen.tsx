import {
  Image,
  StatusBar,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import Header from '@/components/Header.tsx';
import { COLORS } from '@/constants/colors.ts';
import { useTranslation } from 'react-i18next';
import { useEffect, useState } from 'react';
import { getMyUser, UserDto } from '@/api/userApi.ts';
import { useAuth } from '@/context/AuthContext.tsx';
import AppIcon from '@/components/AppIcon/AppIcon.tsx';

function ProfileScreen() {
  const { t } = useTranslation();
  const [user, setUser] = useState<UserDto>();
  const { logout } = useAuth();

  useEffect(() => {
    getMyUser().then(myUser => {
      setUser(myUser);
    });
  }, [setUser]);

  return (
    <SafeAreaView style={styles.safe} edges={['bottom']}>
      <StatusBar barStyle="dark-content" />
      <Header>
        <Text style={styles.title}>{t('profile.title')}</Text>
      </Header>

      <View style={styles.profileContainer}>
        <Image
          source={require('@/assets/images/placeholder-avatar.png')}
          style={styles.avatar}
        />
        <View style={styles.nameEmailContainer}>
          <Text style={styles.userName}>{user?.name}</Text>
          <Text style={styles.email}>{user?.email}</Text>
        </View>
      </View>
      <View style={styles.separator} />

      <TouchableOpacity style={styles.button} onPress={logout}>
        <AppIcon name={'door-open'} size={22} color={COLORS.error} />
        <Text style={styles.buttonText}>Disconnect</Text>
      </TouchableOpacity>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: {
    flex: 1,
    backgroundColor: COLORS.bg,
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
  },
  profileContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    width: '100%',
    paddingHorizontal: 8,
    paddingTop: 16,
    paddingBottom: 10,
  },
  nameEmailContainer: {},
  avatar: {
    width: 80,
    height: 80,
  },
  userName: {
    marginLeft: 12,
    fontWeight: 'bold',
    fontSize: 18,
  },
  email: {
    marginLeft: 12,
    fontSize: 14,
    color: COLORS.gray,
  },
  separator: {
    height: 1,
    backgroundColor: COLORS.border,
    marginTop: 12,
  },
  button: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
    paddingHorizontal: 8,
    paddingVertical: 12,
    borderBottomColor: COLORS.border,
    borderBottomWidth: 1,
  },
  buttonText: {
    fontSize: 16,
    color: COLORS.error,
  },
});

export default ProfileScreen;
